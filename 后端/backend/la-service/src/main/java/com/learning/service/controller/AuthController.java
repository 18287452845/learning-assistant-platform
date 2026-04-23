package com.learning.service.controller;

import com.learning.domain.dto.*;
import com.learning.domain.vo.LoginVO;
import com.learning.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "用户注册、登录相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "支持学生、教师注册账号")
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "账号密码登录")
    @PostMapping("/login")
    public LoginVO login(@Valid @RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    /**
     * 人脸登录
     */
    @Operation(summary = "人脸登录", description = "人脸识别登录（Mock实现）")
    @PostMapping("/face-login")
    public LoginVO faceLogin(@Valid @RequestBody FaceLoginDTO faceLoginDTO) {
        return authService.faceLogin(faceLoginDTO);
    }

    /**
     * 绑定人脸
     */
    @Operation(summary = "绑定人脸", description = "绑定人脸用于登录（Mock实现）")
    @PostMapping("/bind-face")
    public void bindFace(@AuthenticationPrincipal UserDetails userDetails,
                         @Valid @RequestBody FaceBindDTO faceBindDTO) {
        authService.bindFace(getCurrentUserId(userDetails), faceBindDTO);
    }

    /**
     * 解绑人脸
     */
    @Operation(summary = "解绑人脸", description = "解除人脸绑定")
    @PostMapping("/unbind-face")
    public void unbindFace(@AuthenticationPrincipal UserDetails userDetails) {
        authService.unbindFace(getCurrentUserId(userDetails));
    }

    /**
     * 生成验证码
     */
    @Operation(summary = "生成验证码", description = "获取图形验证码")
    @GetMapping("/captcha")
    public Map<String, String> generateCaptcha() {
        return authService.generateCaptcha();
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录", description = "清除登录状态")
    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId(UserDetails userDetails) {
        // 实际应从UserDetails中获取用户ID
        return 1L;
    }
}
