package com.learning.common.exception;

import com.learning.common.constant.Constants;
import com.learning.common.result.Result;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("参数校验异常: {}", message);
        return Result.fail(Constants.ErrorCode.PARAM_ERROR, message);
    }

    /**
     * BindException处理
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("绑定异常: {}", message);
        return Result.fail(Constants.ErrorCode.PARAM_ERROR, message);
    }

    /**
     * ValidationException处理
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(ValidationException e) {
        log.warn("验证异常: {}", e.getMessage());
        return Result.fail(Constants.ErrorCode.PARAM_ERROR, e.getMessage());
    }

    /**
     * 认证异常处理
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleAuthenticationException(AuthenticationException e) {
        log.warn("认证异常: {}", e.getMessage());
        return Result.fail(Constants.ErrorCode.UNAUTHORIZED, "认证失败: " + e.getMessage());
    }

    /**
     * 凭证异常处理
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("凭证异常: {}", e.getMessage());
        return Result.fail(Constants.ErrorCode.UNAUTHORIZED, "用户名或密码错误");
    }

    /**
     * 权限异常处理
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<?> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限异常: {}", e.getMessage());
        return Result.fail(Constants.ErrorCode.FORBIDDEN, "无权限访问");
    }

    /**
     * 文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("文件大小超限: {}", e.getMessage());
        return Result.fail(Constants.ErrorCode.PARAM_ERROR, "文件大小超过限制（最大50MB）");
    }

    /**
     * 空指针异常处理
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.fail(Constants.ErrorCode.FAIL, "系统内部错误");
    }

    /**
     * 运行时异常处理
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常", e);
        return Result.fail(Constants.ErrorCode.FAIL, "系统错误: " + e.getMessage());
    }

    /**
     * 其他异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e) {
        log.error("未知异常", e);
        return Result.fail(Constants.ErrorCode.FAIL, "系统错误，请稍后重试");
    }
}
