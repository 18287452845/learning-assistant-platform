package com.learning.service.controller;

import com.learning.common.result.PageResult;
import com.learning.common.result.Result;
import com.learning.service.UserService;
import com.learning.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 */
@Tag(name = "系统管理", description = "管理员专用接口")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final StatisticsService statisticsService;

    @Operation(summary = "系统统计", description = "获取平台统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        return Result.success(statisticsService.getSystemStatistics());
    }

    @Operation(summary = "用户活跃度统计", description = "统计用户活跃度数据")
    @GetMapping("/statistics/user-activity")
    public Result<Map<String, Object>> getUserActivityStats() {
        return Result.success(statisticsService.getUserActivityStats());
    }

    @Operation(summary = "问答统计", description = "统计问答数据")
    @GetMapping("/statistics/questions")
    public Result<Map<String, Object>> getQuestionStats() {
        return Result.success(statisticsService.getQuestionStats());
    }

    @Operation(summary = "获取操作日志", description = "分页查询操作日志")
    @GetMapping("/logs/operation")
    public Result<PageResult<Map<String, Object>>> getOperationLogs(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(statisticsService.getOperationLogs(keyword, page, size));
    }

    @Operation(summary = "获取登录日志", description = "分页查询登录日志")
    @GetMapping("/logs/login")
    public Result<PageResult<Map<String, Object>>> getLoginLogs(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(statisticsService.getLoginLogs(keyword, page, size));
    }

    @Operation(summary = "健康检查", description = "检查系统健康状态")
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        return Result.success(statisticsService.getHealthStatus());
    }
}
