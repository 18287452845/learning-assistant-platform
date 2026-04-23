package com.learning.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程VO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseVO {

    /**
     * 课程ID
     */
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
     * 分类名称
     */
    private String categoryName;

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
     * 状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
