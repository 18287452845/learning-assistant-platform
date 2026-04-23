package com.learning.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学习记录实体
 */
@Data
@TableName("learning_record")
public class LearningRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 知识点ID
     */
    private Long knowledgeId;

    /**
     * 学习时长(秒)
     */
    private Integer studyDuration;

    /**
     * 学习进度(0-100)
     */
    private Integer progress;

    /**
     * 得分
     */
    private BigDecimal score;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
