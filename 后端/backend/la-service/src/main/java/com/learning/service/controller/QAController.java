package com.learning.service.controller;

import com.learning.common.result.PageResult;
import com.learning.common.result.Result;
import com.learning.domain.dto.QARequestDTO;
import com.learning.domain.vo.QuestionRecordVO;
import com.learning.infra.security.SecurityUtils;
import com.learning.service.QAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 智能问答控制器
 */
@Tag(name = "智能问答", description = "智能问答相关接口")
@RestController
@RequestMapping("/api/qa")
@RequiredArgsConstructor
public class QAController {

    private final QAService qaService;
    private final SecurityUtils securityUtils;

    /**
     * 智能问答
     */
    @Operation(summary = "智能问答", description = "提交问题获取AI答案，支持全网问答和个人问答")
    @PostMapping("/ask")
    public Result<QuestionRecordVO> askQuestion(
            @RequestParam(defaultValue = "global") String type,
            @Valid @RequestBody QARequestDTO requestDTO) {
        Long userId = securityUtils.getCurrentUserId();
        QuestionRecordVO result = qaService.askQuestion(userId, requestDTO);
        return Result.success(result);
    }

    /**
     * 获取问答历史
     */
    @Operation(summary = "获取问答历史", description = "分页查询当前用户的问答记录")
    @GetMapping("/history")
    public Result<PageResult<QuestionRecordVO>> getQuestionHistory(
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = securityUtils.getCurrentUserId();
        PageResult<QuestionRecordVO> result = qaService.getQuestionHistory(userId, courseId, page, size);
        return Result.success(result);
    }

    /**
     * 获取问答详情
     */
    @Operation(summary = "获取问答详情", description = "根据ID获取问答记录详情")
    @GetMapping("/{recordId}")
    public Result<QuestionRecordVO> getQuestionById(@PathVariable Long recordId) {
        Long userId = securityUtils.getCurrentUserId();
        QuestionRecordVO result = qaService.getQuestionById(recordId, userId);
        return Result.success(result);
    }

    /**
     * 评价问答
     */
    @Operation(summary = "评价问答", description = "评价AI回答是否有帮助")
    @PostMapping("/{recordId}/rate")
    public Result<Void> rateQuestion(
            @PathVariable Long recordId,
            @RequestParam Integer helpful) {
        Long userId = securityUtils.getCurrentUserId();
        qaService.rateQuestion(recordId, userId, helpful);
        return Result.success();
    }

    /**
     * 获取课程知识点
     */
    @Operation(summary = "获取课程知识点", description = "获取指定课程的知识点列表")
    @GetMapping("/knowledge")
    public Result<List<Map<String, Object>>> getKnowledgePoints(
            @RequestParam Long courseId) {
        List<Map<String, Object>> result = qaService.getKnowledgePoints(courseId);
        return Result.success(result);
    }

    /**
     * 生成学习计划（Mock实现）
     */
    @Operation(summary = "生成学习计划", description = "根据课程和目标生成个性化学习计划")
    @PostMapping("/plan")
    public Result<Map<String, Object>> generateStudyPlan(
            @RequestParam Long courseId,
            @RequestParam(required = false, defaultValue = "7") Integer days) {
        // Mock实现
        Map<String, Object> plan = Map.of(
            "courseId", courseId,
            "planName", "自定义学习计划",
            "days", days,
            "dailyHours", 2.0,
            "content", List.of(
                Map.of("day", 1, "tasks", List.of("复习基础知识", "完成练习题")),
                Map.of("day", 2, "tasks", List.of("深入学习核心概念", "案例分析")),
                Map.of("day", 3, "tasks", List.of("实践应用", "项目练习")),
                Map.of("day", 4, "tasks", List.of("巩固提高", "查漏补缺")),
                Map.of("day", 5, "tasks", List.of("综合练习", "模拟测试"))
            )
        );
        return Result.success(plan);
    }
}
