package com.learning.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 人脸绑定DTO
 */
@Data
public class FaceBindDTO {

    /**
     * 人脸图片Base64
     */
    @NotBlank(message = "人脸图片不能为空")
    private String faceImage;
}
