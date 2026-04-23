package com.learning.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 人脸登录DTO
 */
@Data
public class FaceLoginDTO {

    /**
     * 人脸图片Base64
     */
    @NotBlank(message = "人脸图片不能为空")
    private String faceImage;
}
