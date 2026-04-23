package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 答疑优先级枚举
 */
@Getter
@AllArgsConstructor
public enum PriorityEnum {

    HIGH(1, "高"),
    MEDIUM(2, "中"),
    LOW(3, "低");

    private final Integer code;
    private final String desc;

    public static PriorityEnum getByCode(Integer code) {
        for (PriorityEnum priorityEnum : values()) {
            if (priorityEnum.getCode().equals(code)) {
                return priorityEnum;
            }
        }
        return MEDIUM;
    }
}
