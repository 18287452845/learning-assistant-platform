package com.learning.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 智能问答DTO
 */
@Data
public class QARequestDTO {

    /**
     * 问题内容
     */
    @NotNull(message = "问题内容不能为空")
    private String question;

    /**
     * 课程ID(可选，用于个人问答)
     */
    private Long courseId;

    /**
     * 问答类型: global-全网问答, personal-个人问答
     */
    private String type = "global";
}
