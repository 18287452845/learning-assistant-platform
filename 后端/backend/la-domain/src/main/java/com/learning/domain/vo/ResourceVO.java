package com.learning.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资源VO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceVO {

    /**
     * 资源ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 文件名
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
     * 文件大小(格式化)
     */
    private String fileSizeDesc;

    /**
     * 审核状态
     */
    private Integer status;

    /**
     * 审核状态描述
     */
    private String statusDesc;

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
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
