package com.learning.common.exception;

import com.learning.common.constant.Constants;
import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;
    private final String message;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        this(Constants.ErrorCode.FAIL, message);
    }

    /**
     * 创建业务异常
     */
    public static BusinessException fail(int code, String message) {
        return new BusinessException(code, message);
    }

    public static BusinessException fail(String message) {
        return new BusinessException(message);
    }

    // 常用异常快捷方法

    /**
     * 参数错误
     */
    public static BusinessException paramError(String message) {
        return fail(Constants.ErrorCode.PARAM_ERROR, message);
    }

    /**
     * 未授权
     */
    public static BusinessException unauthorized(String message) {
        return fail(Constants.ErrorCode.UNAUTHORIZED, message);
    }

    /**
     * 无权限
     */
    public static BusinessException forbidden(String message) {
        return fail(Constants.ErrorCode.FORBIDDEN, message);
    }

    /**
     * 资源不存在
     */
    public static BusinessException notFound(String message) {
        return fail(Constants.ErrorCode.NOT_FOUND, message);
    }

    /**
     * 账号已锁定
     */
    public static BusinessException accountLocked(String message) {
        return fail(Constants.ErrorCode.ACCOUNT_LOCKED, message);
    }

    /**
     * 人脸识别失败
     */
    public static BusinessException faceError(String message) {
        return fail(Constants.ErrorCode.FACE_ERROR, message);
    }
}
