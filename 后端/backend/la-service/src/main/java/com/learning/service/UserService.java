package com.learning.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.common.constant.Constants;
import com.learning.common.exception.BusinessException;
import com.learning.common.result.PageResult;
import com.learning.domain.dto.PasswordUpdateDTO;
import com.learning.domain.dto.UserUpdateDTO;
import com.learning.domain.entity.Role;
import com.learning.domain.entity.User;
import com.learning.domain.vo.UserVO;
import com.learning.infra.mapper.RoleMapper;
import com.learning.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取当前用户信息
     */
    public UserVO getCurrentUser(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        List<String> roles = userMapper.selectRolesByUserId(user.getId());
        return convertToUserVO(user, roles);
    }

    /**
     * 根据ID获取用户信息
     */
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        List<String> roles = userMapper.selectRolesByUserId(user.getId());
        return convertToUserVO(user, roles);
    }

    /**
     * 修改个人信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(String username, UserUpdateDTO updateDTO) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 更新字段
        if (updateDTO.getRealName() != null) {
            user.setRealName(updateDTO.getRealName());
        }
        if (updateDTO.getPhone() != null) {
            // 检查手机号唯一性
            if (userMapper.checkPhoneExists(updateDTO.getPhone()) > 0 
                && !updateDTO.getPhone().equals(user.getPhone())) {
                throw BusinessException.paramError("手机号已被使用");
            }
            user.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getEmail() != null) {
            // 检查邮箱唯一性
            if (userMapper.checkEmailExists(updateDTO.getEmail()) > 0 
                && !updateDTO.getEmail().equals(user.getEmail())) {
                throw BusinessException.paramError("邮箱已被使用");
            }
            user.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getGender() != null) {
            user.setGender(updateDTO.getGender());
        }

        userMapper.updateById(user);
        log.info("用户信息更新成功: username={}", username);
    }

    /**
     * 修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String username, PasswordUpdateDTO passwordDTO) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw BusinessException.paramError("旧密码错误");
        }

        // 验证确认密码
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw BusinessException.paramError("两次密码输入不一致");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userMapper.updateById(user);
        
        log.info("密码修改成功: username={}", username);
    }

    /**
     * 上传头像（Mock实现）
     */
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(String username, MultipartFile file) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // Mock实现：本地存储
        // 实际项目中需要上传到OSS
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String avatarUrl = "/uploads/avatar/" + username + extension;

        user.setAvatar(avatarUrl);
        userMapper.updateById(user);

        log.info("头像上传成功: username={}, avatar={}", username, avatarUrl);
        return avatarUrl;
    }

    /**
     * 分页查询用户列表（管理员）
     */
    public PageResult<UserVO> getUserPage(String keyword, Integer status, Integer page, Integer size) {
        Page<User> pageInfo = new Page<>(page, size);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                .or().like(User::getRealName, keyword)
                .or().like(User::getPhone, keyword)
                .or().like(User::getEmail, keyword));
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        wrapper.eq(User::getDeleted, 0).orderByDesc(User::getCreateTime);
        
        Page<User> result = userMapper.selectPage(pageInfo, wrapper);
        
        List<UserVO> records = result.getRecords().stream()
            .map(user -> {
                List<String> roles = userMapper.selectRolesByUserId(user.getId());
                return convertToUserVO(user, roles);
            })
            .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), (int) result.getCurrent(), 
            (int) result.getSize(), records);
    }

    /**
     * 启用/禁用用户（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        user.setStatus(status);
        userMapper.updateById(user);
        
        log.info("用户状态更新: userId={}, status={}", userId, status);
    }

    /**
     * 重置密码（管理员）
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        // 默认密码为123456
        user.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateById(user);
        
        log.info("密码重置: userId={}", userId);
    }

    /**
     * 转换为UserVO
     */
    private UserVO convertToUserVO(User user, List<String> roles) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setRealName(user.getRealName());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setGender(user.getGender());
        userVO.setGenderDesc(user.getGender() != null ? 
            (user.getGender() == 1 ? "男" : user.getGender() == 2 ? "女" : "未知") : "未知");
        userVO.setRoles(roles);
        userVO.setStatus(user.getStatus());
        userVO.setStatusDesc(user.getStatus() != null && user.getStatus() == 1 ? "正常" : "禁用");
        userVO.setFaceRegistered(user.getFaceRegistered());
        userVO.setLastLoginTime(user.getLastLoginTime());
        userVO.setCreateTime(user.getCreateTime());
        return userVO;
    }
}
