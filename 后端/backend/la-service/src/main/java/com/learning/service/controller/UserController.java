package com.learning.service.controller;

import com.learning.common.result.PageResult;
import com.learning.common.result.Result;
import com.learning.domain.dto.PasswordUpdateDTO;
import com.learning.domain.dto.UserUpdateDTO;
import com.learning.domain.vo.UserVO;
import com.learning.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户控制器
 */
@Tag(name = "用户管理", description = "用户信息管理相关接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<UserVO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserVO user = userService.getCurrentUser(userDetails.getUsername());
        return Result.success(user);
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/{userId}")
    public Result<UserVO> getUserById(@PathVariable Long userId) {
        UserVO user = userService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 修改个人信息
     */
    @Operation(summary = "修改个人信息")
    @PutMapping("/info")
    public Result<Void> updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                   @Valid @RequestBody UserUpdateDTO updateDTO) {
        userService.updateUser(userDetails.getUsername(), updateDTO);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                       @Valid @RequestBody PasswordUpdateDTO passwordDTO) {
        userService.updatePassword(userDetails.getUsername(), passwordDTO);
        return Result.success();
    }

    /**
     * 上传头像
     */
    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(userDetails.getUsername(), file);
        return Result.success(avatarUrl);
    }

    /**
     * 获取用户列表（管理员）
     */
    @Operation(summary = "获取用户列表", description = "分页查询用户列表（管理员专用）")
    @GetMapping("/list")
    public Result<PageResult<UserVO>> getUserPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<UserVO> result = userService.getUserPage(keyword, status, page, size);
        return Result.success(result);
    }

    /**
     * 启用/禁用用户（管理员）
     */
    @Operation(summary = "启用/禁用用户", description = "修改用户状态（管理员专用）")
    @PutMapping("/{userId}/status")
    public Result<Void> updateUserStatus(@PathVariable Long userId,
                                        @RequestParam Integer status) {
        userService.updateUserStatus(userId, status);
        return Result.success();
    }

    /**
     * 重置密码（管理员）
     */
    @Operation(summary = "重置密码", description = "将用户密码重置为123456（管理员专用）")
    @PostMapping("/{userId}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long userId) {
        userService.resetPassword(userId);
        return Result.success();
    }
}
