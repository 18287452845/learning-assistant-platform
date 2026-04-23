package com.learning.domain.vo;

import lombok.Data;

/**
 * 登录响应VO
 */
@Data
public class LoginVO {

    /**
     * 访问Token
     */
    private String accessToken;

    /**
     * 刷新Token
     */
    private String refreshToken;

    /**
     * Token类型
     */
    private String tokenType = "Bearer";

    /**
     * 过期时间(秒)
     */
    private Long expiresIn;

    /**
     * 用户信息
     */
    private UserVO user;
}
