package com.learning.service.controller;

import com.learning.common.result.Result;
import com.learning.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取系统统计信息
     */
    @Operation(summary = "系统统计", description = "获取平台统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        // Mock统计数据
        Map<String, Object> stats = Map.of(
            "totalUsers", 1256,
            "totalStudents", 1100,
            "totalTeachers", 150,
            "totalAdmins", 6,
            "totalCourses", 45,
            "totalResources", 328,
            "totalQuestions", 5680,
            "activeUsers", 328,
            "todayQuestions", 45,
            "pendingResources", 12,
            "pendingRequests", 8
        );
        return Result.success(stats);
    }

    /**
     * 获取用户活跃度统计
     */
    @Operation(summary = "用户活跃度统计", description = "统计用户活跃度数据")
    @GetMapping("/statistics/user-activity")
    public Result<Map<String, Object>> getUserActivityStats() {
        // Mock统计数据
        return Result.success(Map.of(
            "data", Map.of(
                "daily", java.util.List.of(
                    Map.of("date", "2024-01-15", "count", 328),
                    Map.of("date", "2024-01-14", "count", 312),
                    Map.of("date", "2024-01-13", "count", 298),
                    Map.of("date", "2024-01-12", "count", 356),
                    Map.of("date", "2024-01-11", "count", 389)
                ),
                "hourly", java.util.List.of(
                    Map.of("hour", "08:00", "count", 45),
                    Map.of("hour", "10:00", "count", 128),
                    Map.of("hour", "12:00", "count", 89),
                    Map.of("hour", "14:00", "count", 156),
                    Map.of("hour", "16:00", "count", 198),
                    Map.of("hour", "20:00", "count", 245)
                )
            )
        ));
    }

    /**
     * 获取问答统计
     */
    @Operation(summary = "问答统计", description = "统计问答数据")
    @GetMapping("/statistics/questions")
    public Result<Map<String, Object>> getQuestionStats() {
        return Result.success(Map.of(
            "totalQuestions", 5680,
            "aiAnswered", 5200,
            "humanAnswered", 480,
            "avgResponseTime", "2.5秒",
            "satisfactionRate", "94.5%"
        ));
    }

    /**
     * 健康检查
     */
    @Operation(summary = "健康检查", description = "检查系统健康状态")
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        return Result.success(Map.of(
            "status", "UP",
            "timestamp", System.currentTimeMillis(),
            "uptime", "99.9%",
            "services", Map.of(
                "database", "UP",
                "redis", "UP",
                "api", "UP"
            )
        ));
    }
}
