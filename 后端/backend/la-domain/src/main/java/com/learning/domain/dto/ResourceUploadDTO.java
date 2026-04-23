package com.learning.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 资源上传DTO
 */
@Data
public class ResourceUploadDTO {

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 资源标题
     */
    @NotBlank(message = "资源标题不能为空")
    private String title;

    /**
     * 资源描述
     */
    private String description;
}
