package com.learning.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 创建成功结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 创建成功结果
     */
    public static <T> Result<T> success(T data) {
        return success(data, "操作成功");
    }

    /**
     * 创建成功结果
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(200, message, data, System.currentTimeMillis());
    }

    /**
     * 创建失败结果
     */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null, System.currentTimeMillis());
    }

    /**
     * 创建失败结果
     */
    public static <T> Result<T> fail(String message) {
        return fail(500, message);
    }

    /**
     * 创建失败结果
     */
    public static <T> Result<T> fail(int code, String message, T data) {
        return new Result<>(code, message, data, System.currentTimeMillis());
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code == 200;
    }

    /**
     * 判断是否失败
     */
    public boolean isFail() {
        return this.code != 200;
    }
}
