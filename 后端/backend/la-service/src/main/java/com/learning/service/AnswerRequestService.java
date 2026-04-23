package com.learning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.common.constant.Constants;
import com.learning.common.exception.BusinessException;
import com.learning.common.result.PageResult;
import com.learning.domain.dto.AnswerRequestDTO;
import com.learning.domain.dto.AnswerDTO;
import com.learning.domain.entity.Course;
import com.learning.domain.entity.QARequest;
import com.learning.domain.entity.User;
import com.learning.infra.mapper.CourseMapper;
import com.learning.infra.mapper.QARequestMapper;
import com.learning.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 答疑请求服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerRequestService {

    private final QARequestMapper qaRequestMapper;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    /**
     * 提交答疑请求
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitRequest(Long studentId, AnswerRequestDTO requestDTO) {
        // 获取学生信息
        User student = userMapper.selectById(studentId);
        if (student == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 获取课程信息
        Course course = courseMapper.selectById(requestDTO.getCourseId());
        if (course == null) {
            throw BusinessException.notFound("课程不存在");
        }

        // 创建答疑请求
        QARequest qaRequest = new QARequest();
        qaRequest.setStudentId(studentId);
        qaRequest.setStudentName(student.getRealName());
        qaRequest.setCourseId(course.getId());
        qaRequest.setCourseName(course.getCourseName());
        qaRequest.setQuestion(requestDTO.getQuestion());
        qaRequest.setStatus(Constants.QARequestStatus.PENDING);
        qaRequest.setPriority(2); // 默认中等优先级

        // 如果指定了教师
        if (requestDTO.getTeacherId() != null) {
            User teacher = userMapper.selectById(requestDTO.getTeacherId());
            if (teacher != null) {
                qaRequest.setTeacherId(teacher.getId());
                qaRequest.setTeacherName(teacher.getRealName());
            }
        } else {
            // 使用课程教师
            qaRequest.setTeacherId(course.getTeacherId());
            qaRequest.setTeacherName(course.getTeacherName());
        }

        qaRequestMapper.insert(qaRequest);

        log.info("答疑请求提交成功: requestId={}, studentId={}", qaRequest.getId(), studentId);

        return Map.of(
            "id", qaRequest.getId(),
            "status", "pending",
            "message", "答疑请求已提交，请等待教师回复"
        );
    }

    /**
     * 获取待处理答疑列表(教师)
     */
    public PageResult<Map<String, Object>> getPendingRequests(Long teacherId, Integer page, Integer size) {
        Page<QARequest> pageInfo = new Page<>(page, size);
        Page<QARequest> result;

        if (teacherId != null) {
            result = qaRequestMapper.selectPendingByTeacherId(pageInfo, teacherId);
        } else {
            result = qaRequestMapper.selectAllPending(pageInfo);
        }

        return PageResult.of(
            result.getTotal(),
            (int) result.getCurrent(),
            (int) result.getSize(),
            result.getRecords().stream().map(this::convertToMap).toList()
        );
    }

    /**
     * 获取学生的答疑请求列表
     */
    public PageResult<Map<String, Object>> getStudentRequests(Long studentId, Integer page, Integer size) {
        Page<QARequest> pageInfo = new Page<>(page, size);
        Page<QARequest> result = qaRequestMapper.selectByStudentId(pageInfo, studentId);

        return PageResult.of(
            result.getTotal(),
            (int) result.getCurrent(),
            (int) result.getSize(),
            result.getRecords().stream().map(this::convertToMap).toList()
        );
    }

    /**
     * 教师回答问题
     */
    @Transactional(rollbackFor = Exception.class)
    public void answerQuestion(Long requestId, Long teacherId, AnswerDTO answerDTO) {
        QARequest qaRequest = qaRequestMapper.selectById(requestId);
        if (qaRequest == null) {
            throw BusinessException.notFound("答疑请求不存在");
        }

        if (!Constants.QARequestStatus.PENDING.equals(qaRequest.getStatus())) {
            throw BusinessException.paramError("该请求已处理");
        }

        // 验证教师权限
        if (teacherId != null && !teacherId.equals(qaRequest.getTeacherId())) {
            throw BusinessException.forbidden("无权回答此问题");
        }

        qaRequest.setAnswer(answerDTO.getAnswer());
        qaRequest.setAnswerTime(LocalDateTime.now());
        qaRequest.setStatus(Constants.QARequestStatus.ANSWERED);
        qaRequestMapper.updateById(qaRequest);

        log.info("答疑回答成功: requestId={}, teacherId={}", requestId, teacherId);
    }

    /**
     * 关闭答疑请求
     */
    @Transactional(rollbackFor = Exception.class)
    public void closeRequest(Long requestId, Long userId) {
        QARequest qaRequest = qaRequestMapper.selectById(requestId);
        if (qaRequest == null) {
            throw BusinessException.notFound("答疑请求不存在");
        }

        // 学生或教师可以关闭
        if (!qaRequest.getStudentId().equals(userId) && 
            (qaRequest.getTeacherId() == null || !qaRequest.getTeacherId().equals(userId))) {
            throw BusinessException.forbidden("无权关闭此请求");
        }

        qaRequest.setStatus(Constants.QARequestStatus.CLOSED);
        qaRequestMapper.updateById(qaRequest);

        log.info("答疑请求已关闭: requestId={}", requestId);
    }

    /**
     * 获取高频问题统计(管理员/教师)
     */
    public Map<String, Object> getStatistics(Long teacherId) {
        // Mock统计数据
        return Map.of(
            "totalQuestions", 156,
            "pendingQuestions", 12,
            "answeredQuestions", 140,
            "closedQuestions", 4,
            "avgResponseTime", "2.5小时",
            "topQuestions", java.util.List.of(
                Map.of("question", "如何理解递归算法？", "count", 23),
                Map.of("question", "二叉树遍历的实现", "count", 18),
                Map.of("question", "动态规划解题思路", "count", 15),
                Map.of("question", "图的最短路径算法", "count", 12),
                Map.of("question", "排序算法比较", "count", 10)
            )
        );
    }

    /**
     * 转换为Map
     */
    private Map<String, Object> convertToMap(QARequest request) {
        return Map.of(
            "id", request.getId(),
            "studentId", request.getStudentId(),
            "studentName", request.getStudentName(),
            "teacherId", request.getTeacherId() != null ? request.getTeacherId() : "",
            "teacherName", request.getTeacherName() != null ? request.getTeacherName() : "待分配",
            "courseId", request.getCourseId(),
            "courseName", request.getCourseName(),
            "question", request.getQuestion(),
            "answer", request.getAnswer() != null ? request.getAnswer() : "",
            "status", request.getStatus(),
            "statusDesc", getStatusDesc(request.getStatus()),
            "priority", request.getPriority(),
            "createTime", request.getCreateTime(),
            "answerTime", request.getAnswerTime() != null ? request.getAnswerTime() : ""
        );
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待处理";
            case 1: return "已回答";
            case 2: return "已关闭";
            default: return "未知";
        }
    }
}
