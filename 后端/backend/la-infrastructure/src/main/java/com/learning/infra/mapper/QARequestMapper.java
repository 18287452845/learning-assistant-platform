package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.domain.entity.QARequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 答疑请求Mapper接口
 */
@Mapper
public interface QARequestMapper extends BaseMapper<QARequest> {

    /**
     * 更新答疑回答
     */
    int updateAnswer(@Param("id") Long id,
                     @Param("answer") String answer,
                     @Param("answerTime") LocalDateTime answerTime);

    /**
     * 查询教师的待处理请求
     */
    IPage<QARequest> selectPendingByTeacherId(Page<QARequest> page, @Param("teacherId") Long teacherId);

    /**
     * 查询所有待处理请求
     */
    IPage<QARequest> selectAllPending(Page<QARequest> page);

    /**
     * 查询学生的请求
     */
    IPage<QARequest> selectByStudentId(Page<QARequest> page, @Param("studentId") Long studentId);
}
