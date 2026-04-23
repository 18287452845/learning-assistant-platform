package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 掌握程度枚举
 */
@Getter
@AllArgsConstructor
public enum MasteryLevelEnum {

    NOT_MASTERED(0, "未掌握"),
    PARTIALLY_MASTERED(1, "部分掌握"),
    MASTERED(2, "已掌握");

    private final Integer code;
    private final String desc;

    public static MasteryLevelEnum getByCode(Integer code) {
        for (MasteryLevelEnum masteryLevelEnum : values()) {
            if (masteryLevelEnum.getCode().equals(code)) {
                return masteryLevelEnum;
            }
        }
        return NOT_MASTERED;
    }
}
