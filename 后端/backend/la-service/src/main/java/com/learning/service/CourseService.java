package com.learning.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.common.constant.Constants;
import com.learning.common.exception.BusinessException;
import com.learning.common.result.PageResult;
import com.learning.domain.entity.Course;
import com.learning.domain.entity.Resource;
import com.learning.domain.entity.User;
import com.learning.domain.vo.CourseVO;
import com.learning.domain.vo.ResourceVO;
import com.learning.infra.mapper.CourseMapper;
import com.learning.infra.mapper.ResourceMapper;
import com.learning.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 课程服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseMapper courseMapper;
    private final ResourceMapper resourceMapper;
    private final UserMapper userMapper;
    private final FileService fileService;

    /**
     * 获取课程列表
     */
    public PageResult<CourseVO> getCoursePage(String keyword, Long categoryId, Long teacherId, 
                                               Integer status, Integer page, Integer size) {
        Page<Course> pageInfo = new Page<>(page, size);
        
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Course::getCourseName, keyword)
                .or().like(Course::getCourseCode, keyword));
        }
        if (categoryId != null) {
            wrapper.eq(Course::getCategoryId, categoryId);
        }
        if (teacherId != null) {
            wrapper.eq(Course::getTeacherId, teacherId);
        }
        if (status != null) {
            wrapper.eq(Course::getStatus, status);
        } else {
            wrapper.eq(Course::getStatus, Constants.Status.ENABLED);
        }
        wrapper.orderByDesc(Course::getCreateTime);
        
        Page<Course> result = courseMapper.selectPage(pageInfo, wrapper);
        
        List<CourseVO> records = result.getRecords().stream()
            .map(this::convertToCourseVO)
            .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), (int) result.getCurrent(), 
            (int) result.getSize(), records);
    }

    /**
     * 获取课程详情
     */
    public CourseVO getCourseById(Long courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw BusinessException.notFound("课程不存在");
        }
        return convertToCourseVO(course);
    }

    /**
     * 获取资源列表
     */
    public PageResult<ResourceVO> getResourcePage(Long courseId, Long uploaderId, 
                                                   Integer status, Integer page, Integer size) {
        Page<Resource> pageInfo = new Page<>(page, size);
        
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        if (courseId != null) {
            wrapper.eq(Resource::getCourseId, courseId);
        }
        if (uploaderId != null) {
            wrapper.eq(Resource::getUploaderId, uploaderId);
        }
        if (status != null) {
            wrapper.eq(Resource::getStatus, status);
        } else {
            // 默认只显示已审核通过的资源
            wrapper.eq(Resource::getStatus, Constants.ResourceStatus.APPROVED);
        }
        wrapper.orderByDesc(Resource::getCreateTime);
        
        Page<Resource> result = resourceMapper.selectPage(pageInfo, wrapper);
        
        List<ResourceVO> records = result.getRecords().stream()
            .map(this::convertToResourceVO)
            .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), (int) result.getCurrent(), 
            (int) result.getSize(), records);
    }

    /**
     * 获取资源详情
     */
    public ResourceVO getResourceById(Long resourceId) {
        Resource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            throw BusinessException.notFound("资源不存在");
        }
        return convertToResourceVO(resource);
    }

    /**
     * 上传资源
     */
    @Transactional(rollbackFor = Exception.class)
    public ResourceVO uploadResource(Long uploaderId, Long courseId, String title,
                                      String description, MultipartFile file) {
        // 验证课程
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw BusinessException.notFound("课程不存在");
        }

        // 验证用户
        User uploader = userMapper.selectById(uploaderId);
        if (uploader == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 检查用户上传数量
        Long count = resourceMapper.selectCount(
            new LambdaQueryWrapper<Resource>().eq(Resource::getUploaderId, uploaderId)
        );
        if (count >= Constants.File.MAX_RESOURCE_COUNT) {
            throw BusinessException.paramError("已达到最大上传数量限制");
        }

        // 获取文件信息
        String originalFilename = file.getOriginalFilename();
        String fileType = getFileType(originalFilename);

        // 验证文件类型
        if (!isAllowedFileType(fileType)) {
            throw BusinessException.paramError("不支持的文件类型");
        }

        String filePath = fileService.uploadResource(file);

        // 创建资源记录
        Resource resource = new Resource();
        resource.setCourseId(courseId);
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setFilePath(filePath);
        resource.setFileName(originalFilename);
        resource.setFileType(fileType);
        resource.setFileSize(file.getSize());
        resource.setParseStatus(0);
        resource.setVectorStatus(0);
        resource.setDownloadCount(0);
        resource.setStatus(Constants.ResourceStatus.PENDING);
        resource.setUploaderId(uploaderId);
        resource.setUploaderName(uploader.getRealName());
        resourceMapper.insert(resource);

        // 更新课程资源数量
        courseMapper.updateResourceCount(courseId);

        log.info("资源上传成功: resourceId={}, uploaderId={}", resource.getId(), uploaderId);

        return convertToResourceVO(resource);
    }

    /**
     * 删除资源
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteResource(Long resourceId, Long userId) {
        Resource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            throw BusinessException.notFound("资源不存在");
        }

        // 验证权限（只能删除自己的资源）
        if (!resource.getUploaderId().equals(userId)) {
            throw BusinessException.forbidden("无权删除此资源");
        }

        // 删除文件
        fileService.deleteFile(resource.getFilePath());

        // 更新课程资源数量
        courseMapper.updateResourceCount(resource.getCourseId());

        resourceMapper.deleteById(resourceId);

        log.info("资源删除成功: resourceId={}, userId={}", resourceId, userId);
    }

    /**
     * 下载资源
     */
    public String getResourceDownloadUrl(Long resourceId) {
        Resource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            throw BusinessException.notFound("资源不存在");
        }
        
        // 更新下载次数
        resourceMapper.updateDownloadCount(resourceId);
        
        return resource.getFilePath();
    }

    /**
     * 审核资源（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void approveResource(Long resourceId, Integer status, String rejectReason) {
        Resource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            throw BusinessException.notFound("资源不存在");
        }
        
        resource.setStatus(status);
        if (status != null && status == Constants.ResourceStatus.REJECTED) {
            resource.setRejectReason(rejectReason);
        }
        resourceMapper.updateById(resource);
        
        log.info("资源审核完成: resourceId={}, status={}", resourceId, status);
    }

    /**
     * 获取待审核资源列表（管理员）
     */
    public PageResult<ResourceVO> getPendingResources(Integer page, Integer size) {
        Page<Resource> pageInfo = new Page<>(page, size);
        var result = resourceMapper.selectPendingResources(pageInfo);
        
        List<ResourceVO> records = result.getRecords().stream()
            .map(this::convertToResourceVO)
            .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), (int) result.getCurrent(), 
            (int) result.getSize(), records);
    }

    /**
     * 转换为CourseVO
     */
    private CourseVO convertToCourseVO(Course course) {
        CourseVO vo = new CourseVO();
        vo.setId(course.getId());
        vo.setCourseName(course.getCourseName());
        vo.setCourseCode(course.getCourseCode());
        vo.setCategoryId(course.getCategoryId());
        vo.setTeacherId(course.getTeacherId());
        vo.setTeacherName(course.getTeacherName());
        vo.setDescription(course.getDescription());
        vo.setCoverImage(course.getCoverImage());
        vo.setCredit(course.getCredit());
        vo.setStudentCount(course.getStudentCount());
        vo.setResourceCount(course.getResourceCount());
        vo.setStatus(course.getStatus());
        vo.setStatusDesc(course.getStatus() != null && course.getStatus() == 1 ? "上架" : "下架");
        vo.setCreateTime(course.getCreateTime());
        return vo;
    }

    /**
     * 转换为ResourceVO
     */
    private ResourceVO convertToResourceVO(Resource resource) {
        ResourceVO vo = new ResourceVO();
        vo.setId(resource.getId());
        vo.setCourseId(resource.getCourseId());
        vo.setTitle(resource.getTitle());
        vo.setDescription(resource.getDescription());
        vo.setFileName(resource.getFileName());
        vo.setFileType(resource.getFileType());
        vo.setFileSize(resource.getFileSize());
        vo.setFileSizeDesc(formatFileSize(resource.getFileSize()));
        vo.setStatus(resource.getStatus());
        vo.setStatusDesc(getStatusDesc(resource.getStatus()));
        vo.setRejectReason(resource.getRejectReason());
        vo.setUploaderId(resource.getUploaderId());
        vo.setUploaderName(resource.getUploaderName());
        vo.setDownloadCount(resource.getDownloadCount());
        vo.setCreateTime(resource.getCreateTime());
        
        // 获取课程名称
        Course course = courseMapper.selectById(resource.getCourseId());
        if (course != null) {
            vo.setCourseName(course.getCourseName());
        }
        
        return vo;
    }

    /**
     * 获取文件类型
     */
    private String getFileType(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 验证文件类型
     */
    private boolean isAllowedFileType(String fileType) {
        for (String allowed : Constants.File.ALLOWED_TYPES) {
            if (allowed.equalsIgnoreCase(fileType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(Long size) {
        if (size == null || size == 0) {
            return "0 B";
        }
        if (size < 1024) {
            return size + " B";
        }
        if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        }
        if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        }
        return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待审核";
            case 1: return "已通过";
            case 2: return "已驳回";
            default: return "未知";
        }
    }
}
