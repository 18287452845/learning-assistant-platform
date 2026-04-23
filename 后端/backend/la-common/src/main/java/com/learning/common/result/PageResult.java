package com.learning.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 *
 * @param <T> 数据类型
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(long total, int page, int size, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        result.setRecords(records);
        if (size > 0) {
            result.setPages((int) Math.ceil((double) total / size));
        } else {
            result.setPages(0);
        }
        return result;
    }

    /**
     * 创建空分页结果
     */
    public static <T> PageResult<T> empty(int page, int size) {
        return of(0, page, size, List.of());
    }
}
