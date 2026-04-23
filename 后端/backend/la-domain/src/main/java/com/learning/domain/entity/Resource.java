package com.learning.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程资源实体
 */
@Data
@TableName("resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 解析后的文本内容
     */
    private String parsedContent;

    /**
     * 解析状态(0未解析 1解析中 2已完成 3失败)
     */
    private Integer parseStatus;

    /**
     * 向量化状态(0未处理 1处理中 2完成)
     */
    private Integer vectorStatus;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 审核状态(0待审核 1已通过 2已驳回)
     */
    private Integer status;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 上传者ID
     */
    private Long uploaderId;

    /**
     * 上传者姓名
     */
    private String uploaderName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
