package com.learning.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learning.common.constant.Constants;
import com.learning.common.exception.BusinessException;
import com.learning.domain.dto.*;
import com.learning.domain.entity.Role;
import com.learning.domain.entity.User;
import com.learning.domain.entity.UserRole;
import com.learning.domain.vo.LoginVO;
import com.learning.domain.vo.UserVO;
import com.learning.infra.mapper.RoleMapper;
import com.learning.infra.mapper.UserMapper;
import com.learning.infra.mapper.UserRoleMapper;
import com.learning.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) {
        // 验证确认密码
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw BusinessException.paramError("两次密码输入不一致");
        }

        // 验证用户名唯一性
        if (userMapper.checkUsernameExists(registerDTO.getUsername()) > 0) {
            throw BusinessException.paramError("用户名已存在");
        }

        // 验证手机号唯一性
        if (registerDTO.getPhone() != null && userMapper.checkPhoneExists(registerDTO.getPhone()) > 0) {
            throw BusinessException.paramError("手机号已被使用");
        }

        // 验证邮箱唯一性
        if (registerDTO.getEmail() != null && userMapper.checkEmailExists(registerDTO.getEmail()) > 0) {
            throw BusinessException.paramError("邮箱已被使用");
        }

        // 查询角色
        Role role = roleMapper.selectByRoleKey(registerDTO.getRole());
        if (role == null) {
            throw BusinessException.paramError("角色不存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRealName(registerDTO.getRealName());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setStatus(Constants.Status.ENABLED);
        user.setFaceRegistered(0);
        user.setLoginCount(0);
        userMapper.insert(user);

        // 分配角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        userRoleMapper.insert(userRole);

        log.info("用户注册成功: username={}, role={}", user.getUsername(), registerDTO.getRole());
    }

    /**
     * 用户登录
     */
    public LoginVO login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String token = jwtTokenProvider.generateToken(authentication);
            
            // 获取用户信息
            User user = userMapper.selectByUsername(loginDTO.getUsername());
            
            // 更新登录信息
            userMapper.updateLoginInfo(user.getId(), null);
            
            // 构建返回结果
            return buildLoginVO(user, token);
        } catch (BadCredentialsException e) {
            log.warn("登录失败: username={}, reason=密码错误", loginDTO.getUsername());
            throw BusinessException.unauthorized("用户名或密码错误");
        }
    }

    /**
     * 人脸登录（Mock实现）
     */
    public LoginVO faceLogin(FaceLoginDTO faceLoginDTO) {
        // Mock实现：模拟百度云人脸识别API调用
        // 实际项目中需要调用百度云API进行人脸识别
        log.info("人脸登录请求");

        // 模拟查找已注册人脸的用户
        User user = userMapper.selectList(
            new LambdaQueryWrapper<User>()
                .eq(User::getFaceRegistered, 1)
                .eq(User::getStatus, Constants.Status.ENABLED)
        ).stream().findFirst().orElse(null);

        if (user == null) {
            throw BusinessException.faceError("未找到绑定的人脸或用户已被禁用");
        }

        // 生成Token
        String token = jwtTokenProvider.generateToken(user.getUsername());

        // 更新登录信息
        userMapper.updateLoginInfo(user.getId(), null);

        log.info("人脸登录成功: userId={}", user.getId());

        return buildLoginVO(user, token);
    }

    /**
     * 绑定人脸（Mock实现）
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindFace(Long userId, FaceBindDTO faceBindDTO) {
        // Mock实现：模拟百度云人脸注册API调用
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 模拟生成face_token
        String faceToken = IdUtil.simpleUUID();
        
        // 更新用户人脸信息
        user.setFaceToken(faceToken);
        user.setFaceRegistered(1);
        userMapper.updateById(user);

        log.info("人脸绑定成功: userId={}", userId);
    }

    /**
     * 解绑人脸
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindFace(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        user.setFaceToken(null);
        user.setFaceRegistered(0);
        userMapper.updateById(user);

        log.info("人脸解绑成功: userId={}", userId);
    }

    /**
     * 生成图形验证码
     */
    public Map<String, String> generateCaptcha() {
        // 使用Hutool生成图形验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40, 4, 30);
        String code = captcha.getCode();
        String imageBase64 = captcha.getImageBase64();
        
        // 生成验证码Key
        String key = IdUtil.simpleUUID();
        
        // 存入Redis，5分钟有效
        redisTemplate.opsForValue().set(
            Constants.CacheKey.CAPTCHA + key, 
            code, 
            5, 
            TimeUnit.MINUTES
        );
        
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        result.put("image", "data:image/png;base64," + imageBase64);
        
        return result;
    }

    /**
     * 退出登录
     */
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    /**
     * 构建登录返回结果
     */
    private LoginVO buildLoginVO(User user, String token) {
        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(token);
        loginVO.setTokenType("Bearer");
        loginVO.setExpiresIn(jwtTokenProvider.getExpirationTime() / 1000);
        
        // 获取角色列表
        List<String> roles = userMapper.selectRolesByUserId(user.getId());
        
        // 构建用户信息
        UserVO userVO = convertToUserVO(user, roles);
        loginVO.setUser(userVO);
        
        return loginVO;
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
