package com.learning.service.controller;

import com.learning.common.result.PageResult;
import com.learning.common.result.Result;
import com.learning.domain.dto.AnswerRequestDTO;
import com.learning.domain.dto.AnswerDTO;
import com.learning.infra.security.SecurityUtils;
import com.learning.service.AnswerRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 答疑请求控制器
 */
@Tag(name = "答疑请求", description = "人工答疑相关接口")
@RestController
@RequestMapping("/api/qa-request")
@RequiredArgsConstructor
public class AnswerRequestController {

    private final AnswerRequestService answerRequestService;
    private final SecurityUtils securityUtils;

    /**
     * 提交答疑请求
     */
    @Operation(summary = "提交答疑请求", description = "学生提交人工答疑请求")
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public Result<Map<String, Object>> submitRequest(
            @RequestParam Long courseId,
            @RequestParam String question,
            @RequestParam(required = false) Long teacherId) {
        Long studentId = securityUtils.getCurrentUserId();
        AnswerRequestDTO requestDTO = new AnswerRequestDTO();
        requestDTO.setCourseId(courseId);
        requestDTO.setQuestion(question);
        requestDTO.setTeacherId(teacherId);
        Map<String, Object> result = answerRequestService.submitRequest(studentId, requestDTO);
        return Result.success(result);
    }

    /**
     * 获取待处理答疑列表
     */
    @Operation(summary = "获取待处理答疑列表", description = "教师获取待处理的答疑请求列表")
    @GetMapping("/pending")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getPendingRequests(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<Map<String, Object>> result = answerRequestService.getPendingRequests(teacherId, page, size);
        return Result.success(result);
    }

    /**
     * 获取学生的答疑请求列表
     */
    @Operation(summary = "获取学生的答疑请求", description = "学生查看自己的答疑请求列表")
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<PageResult<Map<String, Object>>> getMyRequests(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long studentId = securityUtils.getCurrentUserId();
        PageResult<Map<String, Object>> result = answerRequestService.getStudentRequests(studentId, page, size);
        return Result.success(result);
    }

    /**
     * 回答问题
     */
    @Operation(summary = "回答问题", description = "教师回答学生的答疑请求")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Void> answerQuestion(
            @PathVariable Long id,
            @Valid @RequestBody AnswerDTO answerDTO) {
        Long teacherId = securityUtils.getCurrentUserId();
        answerRequestService.answerQuestion(id, teacherId, answerDTO);
        return Result.success();
    }

    /**
     * 关闭答疑请求
     */
    @Operation(summary = "关闭答疑请求", description = "学生或教师关闭答疑请求")
    @PostMapping("/{id}/close")
    public Result<Void> closeRequest(@PathVariable Long id) {
        Long userId = securityUtils.getCurrentUserId();
        answerRequestService.closeRequest(id, userId);
        return Result.success();
    }

    /**
     * 获取高频问题统计
     */
    @Operation(summary = "高频问题统计", description = "统计高频问题和答疑数据")
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public Result<Map<String, Object>> getStatistics(
            @RequestParam(required = false) Long teacherId) {
        Map<String, Object> result = answerRequestService.getStatistics(teacherId);
        return Result.success(result);
    }
}
