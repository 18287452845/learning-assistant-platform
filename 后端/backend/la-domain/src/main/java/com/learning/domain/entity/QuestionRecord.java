package com.learning.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问答记录实体
 */
@Data
@TableName("question_record")
public class QuestionRecord implements Serializable {

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
     * 用户姓名
     */
    private String userName;

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
     * 全网问答答案
     */
    private String globalAnswer;

    /**
     * 个人问答答案
     */
    private String personalAnswer;

    /**
     * 识别的薄弱知识点
     */
    private String weakPoints;

    /**
     * 推荐学习资源
     */
    private String recommendations;

    /**
     * 答案类型(1AI生成 2人工回答)
     */
    private Integer answerType;

    /**
     * 是否有帮助(0否 1是)
     */
    private Integer helpful;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
