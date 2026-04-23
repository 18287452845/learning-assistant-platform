package com.learning.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问答记录VO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionRecordVO {

    /**
     * 记录ID
     */
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
     * 答案类型
     */
    private Integer answerType;

    /**
     * 答案类型描述
     */
    private String answerTypeDesc;

    /**
     * 是否有帮助
     */
    private Integer helpful;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
