package com.learning.common.constant;

import java.util.List;

/**
 * 全局常量类
 */
public class Constants {

    /**
     * JWT相关常量
     */
    public static class Jwt {
        /**
         * Token请求头名称
         */
        public static final String TOKEN_HEADER = "Authorization";
        
        /**
         * Token前缀
         */
        public static final String TOKEN_PREFIX = "Bearer ";
        
        /**
         * Token有效期（24小时）
         */
        public static final long EXPIRATION = 86400000L;
        
        /**
         * 刷新Token有效期（7天）
         */
        public static final long REFRESH_EXPIRATION = 604800000L;
    }

    /**
     * 角色相关常量
     */
    public static class Role {
        /**
         * 学生角色
         */
        public static final String STUDENT = "STUDENT";
        
        /**
         * 教师角色
         */
        public static final String TEACHER = "TEACHER";
        
        /**
         * 管理员角色
         */
        public static final String ADMIN = "ADMIN";
        
        /**
         * 学生角色ID
         */
        public static final Long STUDENT_ID = 1L;
        
        /**
         * 教师角色ID
         */
        public static final Long TEACHER_ID = 2L;
        
        /**
         * 管理员角色ID
         */
        public static final Long ADMIN_ID = 3L;
    }

    /**
     * 状态相关常量
     */
    public static class Status {
        /**
         * 禁用
         */
        public static final Integer DISABLED = 0;
        
        /**
         * 启用
         */
        public static final Integer ENABLED = 1;
        
        /**
         * 删除
         */
        public static final Integer DELETED = 1;
        
        /**
         * 正常
         */
        public static final Integer NORMAL = 0;
    }

    /**
     * 资源审核状态
     */
    public static class ResourceStatus {
        /**
         * 待审核
         */
        public static final Integer PENDING = 0;
        
        /**
         * 已通过
         */
        public static final Integer APPROVED = 1;
        
        /**
         * 已驳回
         */
        public static final Integer REJECTED = 2;
    }

    /**
     * 问答类型
     */
    public static class AnswerType {
        /**
         * AI生成
         */
        public static final Integer AI = 1;
        
        /**
         * 人工回答
         */
        public static final Integer HUMAN = 2;
    }

    /**
     * 答疑请求状态
     */
    public static class QARequestStatus {
        /**
         * 待处理
         */
        public static final Integer PENDING = 0;
        
        /**
         * 已回答
         */
        public static final Integer ANSWERED = 1;
        
        /**
         * 已关闭
         */
        public static final Integer CLOSED = 2;
    }

    /**
     * 文件上传相关常量
     */
    public static class File {
        /**
         * 允许的文件类型
         */
        public static final String[] ALLOWED_TYPES = {
            "pdf", "doc", "docx", "ppt", "pptx", "txt"
        };
        
        /**
         * 允许的文件类型列表
         */
        public static final List<String> ALLOWED_TYPESList = java.util.Arrays.asList(ALLOWED_TYPES);
        
        /**
         * 最大文件大小（50MB）
         */
        public static final long MAX_SIZE = 50 * 1024 * 1024;
        
        /**
         * 用户最大资源数
         */
        public static final int MAX_RESOURCE_COUNT = 100;
    }

    /**
     * Redis缓存Key前缀
     */
    public static class CacheKey {
        /**
         * 用户信息缓存
         */
        public static final String USER_INFO = "user:info:";
        
        /**
         * 用户权限缓存
         */
        public static final String USER_PERMS = "user:perms:";
        
        /**
         * 验证码缓存
         */
        public static final String CAPTCHA = "captcha:";
        
        /**
         * 登录失败次数
         */
        public static final String LOGIN_FAIL_COUNT = "login:fail:";
        
        /**
         * 登录锁定
         */
        public static final String LOGIN_LOCK = "login:lock:";
    }

    /**
     * 业务错误码
     */
    public static class ErrorCode {
        /**
         * 成功
         */
        public static final int SUCCESS = 200;
        
        /**
         * 失败
         */
        public static final int FAIL = 500;
        
        /**
         * 参数错误
         */
        public static final int PARAM_ERROR = 400;
        
        /**
         * 未授权
         */
        public static final int UNAUTHORIZED = 401;
        
        /**
         * 无权限
         */
        public static final int FORBIDDEN = 403;
        
        /**
         * 资源不存在
         */
        public static final int NOT_FOUND = 404;
        
        /**
         * 账号已锁定
         */
        public static final int ACCOUNT_LOCKED = 421;
        
        /**
         * 验证码错误
         */
        public static final int CAPTCHA_ERROR = 422;
        
        /**
         * 人脸识别失败
         */
        public static final int FACE_ERROR = 423;
    }

    /**
     * 分页默认值
     */
    public static class Page {
        /**
         * 默认页码
         */
        public static final int DEFAULT_PAGE = 1;
        
        /**
         * 默认每页大小
         */
        public static final int DEFAULT_SIZE = 10;
        
        /**
         * 最大每页大小
         */
        public static final int MAX_SIZE = 100;
    }
}
