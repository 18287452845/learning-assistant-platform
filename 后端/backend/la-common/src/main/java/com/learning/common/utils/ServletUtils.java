package com.learning.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Servlet工具类
 */
public class ServletUtils {

    /**
     * 获取HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 获取请求URL
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getRequestURL().toString() : "";
    }

    /**
     * 获取请求方法
     */
    public static String getRequestMethod() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getMethod() : "";
    }

    /**
     * 获取请求参数
     */
    public static String getQueryString() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getQueryString() : "";
    }

    /**
     * 获取用户IP
     */
    public static String getClientIp() {
        HttpServletRequest request = getRequest();
        return request != null ? CommonUtils.getClientIp(request) : "";
    }
}
