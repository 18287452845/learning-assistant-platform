package com.learning.service.controller;

import com.learning.common.result.PageResult;
import com.learning.common.result.Result;
import com.learning.domain.vo.CourseVO;
import com.learning.domain.vo.ResourceVO;
import com.learning.infra.security.SecurityUtils;
import com.learning.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 课程资源控制器
 */
@Tag(name = "课程资源管理", description = "课程和资源相关接口")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final SecurityUtils securityUtils;

    // ==================== 课程接口 ====================

    /**
     * 获取课程列表
     */
    @Operation(summary = "获取课程列表", description = "分页查询课程列表")
    @GetMapping("/courses")
    public Result<PageResult<CourseVO>> getCoursePage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<CourseVO> result = courseService.getCoursePage(keyword, categoryId, teacherId, status, page, size);
        return Result.success(result);
    }

    /**
     * 获取课程详情
     */
    @Operation(summary = "获取课程详情", description = "根据ID获取课程详细信息")
    @GetMapping("/courses/{courseId}")
    public Result<CourseVO> getCourseById(@PathVariable Long courseId) {
        CourseVO course = courseService.getCourseById(courseId);
        return Result.success(course);
    }

    // ==================== 资源接口 ====================

    /**
     * 获取资源列表
     */
    @Operation(summary = "获取资源列表", description = "分页查询课程资源")
    @GetMapping("/resources")
    public Result<PageResult<ResourceVO>> getResourcePage(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long uploaderId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<ResourceVO> result = courseService.getResourcePage(courseId, uploaderId, status, page, size);
        return Result.success(result);
    }

    /**
     * 获取资源详情
     */
    @Operation(summary = "获取资源详情", description = "根据ID获取资源详细信息")
    @GetMapping("/resources/{resourceId}")
    public Result<ResourceVO> getResourceById(@PathVariable Long resourceId) {
        ResourceVO resource = courseService.getResourceById(resourceId);
        return Result.success(resource);
    }

    /**
     * 上传资源
     */
    @Operation(summary = "上传资源", description = "上传课程资源文件")
    @PostMapping("/resources")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public Result<ResourceVO> uploadResource(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam("file") MultipartFile file) {
        Long uploaderId = securityUtils.getCurrentUserId();
        ResourceVO resource = courseService.uploadResource(uploaderId, courseId, title, description, file);
        return Result.success(resource);
    }

    /**
     * 删除资源
     */
    @Operation(summary = "删除资源", description = "删除自己上传的资源")
    @DeleteMapping("/resources/{resourceId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public Result<Void> deleteResource(@PathVariable Long resourceId) {
        Long userId = securityUtils.getCurrentUserId();
        courseService.deleteResource(resourceId, userId);
        return Result.success();
    }

    /**
     * 下载资源
     */
    @Operation(summary = "下载资源", description = "获取资源下载链接")
    @GetMapping("/resources/{resourceId}/download")
    public Result<String> downloadResource(@PathVariable Long resourceId) {
        String downloadUrl = courseService.getResourceDownloadUrl(resourceId);
        return Result.success(downloadUrl);
    }

    // ==================== 管理员接口 ====================

    /**
     * 获取待审核资源列表（管理员）
     */
    @Operation(summary = "获取待审核资源列表", description = "分页查询待审核资源（管理员专用）")
    @GetMapping("/admin/resources/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<ResourceVO>> getPendingResources(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<ResourceVO> result = courseService.getPendingResources(page, size);
        return Result.success(result);
    }

    /**
     * 审核资源（管理员）
     */
    @Operation(summary = "审核资源", description = "通过或驳回资源（管理员专用）")
    @PutMapping("/admin/resources/{resourceId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> approveResource(
            @PathVariable Long resourceId,
            @RequestParam Integer status,
            @RequestParam(required = false) String rejectReason) {
        courseService.approveResource(resourceId, status, rejectReason);
        return Result.success();
    }
}
