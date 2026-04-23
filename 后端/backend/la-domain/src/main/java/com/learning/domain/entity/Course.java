package com.learning.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程实体
 */
@Data
@TableName("course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程代码
     */
    private String courseCode;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 学分
     */
    private BigDecimal credit;

    /**
     * 学生数量
     */
    private Integer studentCount;

    /**
     * 资源数量
     */
    private Integer resourceCount;

    /**
     * 状态(0下架 1上架)
     */
    private Integer status;

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
