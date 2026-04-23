package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final Integer code;
    private final String desc;

    public static GenderEnum getByCode(Integer code) {
        for (GenderEnum genderEnum : values()) {
            if (genderEnum.getCode().equals(code)) {
                return genderEnum;
            }
        }
        return UNKNOWN;
    }
}
