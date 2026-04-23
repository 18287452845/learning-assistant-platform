package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件解析状态枚举
 */
@Getter
@AllArgsConstructor
public enum ParseStatusEnum {

    NOT_PARSED(0, "未解析"),
    PARSING(1, "解析中"),
    COMPLETED(2, "已完成"),
    FAILED(3, "失败");

    private final Integer code;
    private final String desc;

    public static ParseStatusEnum getByCode(Integer code) {
        for (ParseStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}
