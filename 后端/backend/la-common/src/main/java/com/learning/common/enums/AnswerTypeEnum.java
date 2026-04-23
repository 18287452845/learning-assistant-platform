package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 答案类型枚举
 */
@Getter
@AllArgsConstructor
public enum AnswerTypeEnum {

    AI(1, "AI生成"),
    HUMAN(2, "人工回答");

    private final Integer code;
    private final String desc;

    public static AnswerTypeEnum getByCode(Integer code) {
        for (AnswerTypeEnum answerTypeEnum : values()) {
            if (answerTypeEnum.getCode().equals(code)) {
                return answerTypeEnum;
            }
        }
        return null;
    }
}
