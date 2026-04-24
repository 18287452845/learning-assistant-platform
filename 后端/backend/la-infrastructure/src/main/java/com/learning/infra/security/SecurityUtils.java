package com.learning.infra.security;

import com.learning.common.exception.BusinessException;
import com.learning.domain.entity.User;
import com.learning.infra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserMapper userMapper;

    public Long getCurrentUserId() {
        String username = getCurrentUsername();
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw BusinessException.unauthorized("用户不存在");
        }
        return user.getId();
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw BusinessException.unauthorized("用户未登录");
        }
        return auth.getName();
    }
}
