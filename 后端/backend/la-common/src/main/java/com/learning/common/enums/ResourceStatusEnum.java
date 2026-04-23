package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源审核状态枚举
 */
@Getter
@AllArgsConstructor
public enum ResourceStatusEnum {

    PENDING(0, "待审核"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已驳回");

    private final Integer code;
    private final String desc;

    public static ResourceStatusEnum getByCode(Integer code) {
        for (ResourceStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}
