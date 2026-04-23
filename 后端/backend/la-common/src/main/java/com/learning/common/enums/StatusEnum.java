package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    DISABLED(0, "禁用"),
    ENABLED(1, "启用"),
    DELETED(1, "已删除"),
    NORMAL(0, "正常");

    private final Integer code;
    private final String desc;

    /**
     * 根据状态码获取枚举
     */
    public static StatusEnum getByCode(Integer code) {
        for (StatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}
