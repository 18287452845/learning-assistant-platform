package com.learning.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户更新DTO
 */
@Data
public class UserUpdateDTO {

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别(0未知 1男 2女)
     */
    private Integer gender;
}
