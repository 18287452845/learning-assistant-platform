package com.learning.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识点实体
 */
@Data
@TableName("knowledge_point")
public class KnowledgePoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 知识点ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 知识点标题
     */
    private String title;

    /**
     * 知识点内容
     */
    private String content;

    /**
     * 关键词(JSON数组)
     */
    private String keywords;

    /**
     * 父知识点ID
     */
    private Long parentId;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 重要程度(1一般 2重要 3核心)
     */
    private Integer importance;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
