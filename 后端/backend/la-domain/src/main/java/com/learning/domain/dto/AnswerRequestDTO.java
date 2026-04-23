package com.learning.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 答疑请求DTO
 */
@Data
public class AnswerRequestDTO {

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 问题内容
     */
    @NotNull(message = "问题内容不能为空")
    private String question;

    /**
     * 教师ID(可选)
     */
    private Long teacherId;
}
