package com.learning.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.common.constant.Constants;
import com.learning.common.exception.BusinessException;
import com.learning.common.result.PageResult;
import com.learning.domain.dto.QARequestDTO;
import com.learning.domain.entity.*;
import com.learning.domain.vo.QuestionRecordVO;
import com.learning.infra.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 问答服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QAService {

    private final QuestionRecordMapper questionRecordMapper;
    private final CourseMapper courseMapper;
    private final KnowledgePointMapper knowledgePointMapper;
    private final UserMapper userMapper;

    /**
     * 智能问答（Mock LLM调用）
     */
    @Transactional(rollbackFor = Exception.class)
    public QuestionRecordVO askQuestion(Long userId, QARequestDTO requestDTO) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 验证问题长度
        if (requestDTO.getQuestion().length() < 10 || requestDTO.getQuestion().length() > 500) {
            throw BusinessException.paramError("问题长度需在10-500字之间");
        }

        // 获取课程信息
        Course course = null;
        if (requestDTO.getCourseId() != null) {
            course = courseMapper.selectById(requestDTO.getCourseId());
        }

        // Mock实现：模拟LLM生成答案
        String globalAnswer = mockGlobalAnswer(requestDTO.getQuestion());
        String personalAnswer = mockPersonalAnswer(requestDTO.getQuestion(), user.getRealName(), course);

        // 识别薄弱知识点
        List<String> weakPoints = identifyWeakPoints(requestDTO.getQuestion());
        
        // 推荐学习资源
        List<String> recommendations = generateRecommendations(weakPoints);

        // 创建问答记录
        QuestionRecord record = new QuestionRecord();
        record.setUserId(userId);
        record.setUserName(user.getRealName());
        if (course != null) {
            record.setCourseId(course.getId());
            record.setCourseName(course.getCourseName());
        }
        record.setQuestion(requestDTO.getQuestion());
        record.setGlobalAnswer(globalAnswer);
        record.setPersonalAnswer(personalAnswer);
        record.setWeakPoints(JSONUtil.toJsonStr(weakPoints));
        record.setRecommendations(JSONUtil.toJsonStr(recommendations));
        record.setAnswerType(Constants.AnswerType.AI);
        questionRecordMapper.insert(record);

        log.info("智能问答成功: userId={}, recordId={}", userId, record.getId());

        return convertToQuestionRecordVO(record);
    }

    /**
     * 获取问答历史
     */
    public PageResult<QuestionRecordVO> getQuestionHistory(Long userId, Long courseId, 
                                                            Integer page, Integer size) {
        Page<QuestionRecord> pageInfo = new Page<>(page, size);
        Page<QuestionRecord> result = questionRecordMapper.selectQuestionPage(
            pageInfo, userId, courseId);
        
        List<QuestionRecordVO> records = result.getRecords().stream()
            .map(this::convertToQuestionRecordVO)
            .collect(java.util.stream.Collectors.toList());
        
        return PageResult.of(result.getTotal(), (int) result.getCurrent(), 
            (int) result.getSize(), records);
    }

    /**
     * 获取问答详情
     */
    public QuestionRecordVO getQuestionById(Long recordId, Long userId) {
        QuestionRecord record = questionRecordMapper.selectById(recordId);
        if (record == null) {
            throw BusinessException.notFound("问答记录不存在");
        }
        
        // 验证权限（只能查看自己的记录）
        if (!record.getUserId().equals(userId)) {
            throw BusinessException.forbidden("无权查看此记录");
        }
        
        return convertToQuestionRecordVO(record);
    }

    /**
     * 评价问答是否有用
     */
    @Transactional(rollbackFor = Exception.class)
    public void rateQuestion(Long recordId, Long userId, Integer helpful) {
        QuestionRecord record = questionRecordMapper.selectById(recordId);
        if (record == null) {
            throw BusinessException.notFound("问答记录不存在");
        }
        
        if (!record.getUserId().equals(userId)) {
            throw BusinessException.forbidden("无权评价此记录");
        }
        
        record.setHelpful(helpful);
        questionRecordMapper.updateById(record);
        
        log.info("问答评价: recordId={}, helpful={}", recordId, helpful);
    }

    /**
     * 获取课程知识点列表
     */
    public List<Map<String, Object>> getKnowledgePoints(Long courseId) {
        List<KnowledgePoint> points = knowledgePointMapper.selectByCourseId(courseId);
        
        return points.stream().map(point -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", point.getId());
            map.put("title", point.getTitle());
            map.put("content", point.getContent());
            map.put("level", point.getLevel());
            map.put("importance", point.getImportance());
            map.put("importanceDesc", getImportanceDesc(point.getImportance()));
            return map;
        }).collect(java.util.stream.Collectors.toList());
    }

    /**
     * Mock：生成全网问答答案
     */
    private String mockGlobalAnswer(String question) {
        return String.format(
            "【全网问答】\n\n" +
            "根据您的问题，我来为您提供一个全面的解答：\n\n" +
            "%s\n\n" +
            "这个问题涉及多个方面的知识。从通用角度来看，我们需要从基础概念开始理解，\n" +
            "然后逐步深入到具体应用场景。\n\n" +
            "1. 首先，需要明确问题的核心概念\n" +
            "2. 其次，了解相关的理论框架\n" +
            "3. 最后，结合实际案例进行分析\n\n" +
            "希望这个回答对您有所帮助！如需更详细的解答，请继续提问。",
            question
        );
    }

    /**
     * Mock：生成个人问答答案
     */
    private String mockPersonalAnswer(String question, String userName, Course course) {
        String courseInfo = course != null ? 
            String.format("根据您选修的课程《%s》", course.getCourseName()) : 
            "根据您的学习记录";
        
        return String.format(
            "【个人问答】\n\n" +
            "%s，我来为您解答：\n\n" +
            "亲爱的%s同学，您好！\n\n" +
            "针对您提出的问题：%s\n\n" +
            "结合您的学习进度和历史记录，我为您定制了以下解答：\n\n" +
            "📚 相关知识点：\n" +
            "• 基础概念理解\n" +
            "• 核心原理解析\n" +
            "• 实践应用指导\n\n" +
            "💡 学习建议：\n" +
            "1. 先理解基本概念\n" +
            "2. 结合课程资料深入学习\n" +
            "3. 多做练习巩固知识\n\n" +
            "📖 推荐学习资源已为您准备好，请查看下方推荐内容。\n\n" +
            "继续加油！如有疑问，随时向我提问。",
            courseInfo, userName, question
        );
    }

    /**
     * Mock：识别薄弱知识点
     */
    private List<String> identifyWeakPoints(String question) {
        // 简单Mock实现
        List<String> points = new ArrayList<>();
        
        if (question.contains("算法") || question.contains("排序")) {
            points.add("算法基础");
            points.add("时间复杂度");
        }
        if (question.contains("数据库") || question.contains("SQL")) {
            points.add("SQL查询优化");
            points.add("数据库设计");
        }
        if (question.contains("网络") || question.contains("HTTP")) {
            points.add("网络协议");
            points.add("TCP/IP协议栈");
        }
        
        if (points.isEmpty()) {
            points.add("基础知识巩固");
        }
        
        return points;
    }

    /**
     * Mock：生成推荐资源
     */
    private List<String> generateRecommendations(List<String> weakPoints) {
        List<String> recommendations = new ArrayList<>();
        
        for (String point : weakPoints) {
            recommendations.add(String.format("《%s专项练习》", point));
            recommendations.add(String.format("《%s视频教程》", point));
        }
        
        return recommendations;
    }

    /**
     * 转换为QuestionRecordVO
     */
    private QuestionRecordVO convertToQuestionRecordVO(QuestionRecord record) {
        QuestionRecordVO vo = new QuestionRecordVO();
        vo.setId(record.getId());
        vo.setUserId(record.getUserId());
        vo.setUserName(record.getUserName());
        vo.setCourseId(record.getCourseId());
        vo.setCourseName(record.getCourseName());
        vo.setQuestion(record.getQuestion());
        vo.setGlobalAnswer(record.getGlobalAnswer());
        vo.setPersonalAnswer(record.getPersonalAnswer());
        vo.setWeakPoints(record.getWeakPoints());
        vo.setRecommendations(record.getRecommendations());
        vo.setAnswerType(record.getAnswerType());
        vo.setAnswerTypeDesc(record.getAnswerType() != null && 
            record.getAnswerType() == 1 ? "AI生成" : "人工回答");
        vo.setHelpful(record.getHelpful());
        vo.setCreateTime(record.getCreateTime());
        return vo;
    }

    /**
     * 获取重要程度描述
     */
    private String getImportanceDesc(Integer importance) {
        if (importance == null) return "一般";
        switch (importance) {
            case 1: return "一般";
            case 2: return "重要";
            case 3: return "核心";
            default: return "一般";
        }
    }
}
