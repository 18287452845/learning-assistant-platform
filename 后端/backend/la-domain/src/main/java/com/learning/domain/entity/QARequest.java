package com.learning.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 答疑请求实体
 */
@Data
@TableName("qa_request")
public class QARequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 问题内容
     */
    private String question;

    /**
     * 回答内容
     */
    private String answer;

    /**
     * 状态(0待处理 1已回答 2已关闭)
     */
    private Integer status;

    /**
     * 优先级(1高 2中 3低)
     */
    private Integer priority;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 回答时间
     */
    private LocalDateTime answerTime;
}
