package com.learning.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册DTO
 */
@Data
public class RegisterDTO {

    /**
     * 用户名(学号/工号)
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^\\d{6,10}$", message = "学号/工号格式不正确")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "密码至少8位")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "密码必须包含字母和数字")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 角色(STUDENT/TEACHER)
     */
    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "^(STUDENT|TEACHER)$", message = "角色只能是STUDENT或TEACHER")
    private String role;
}
