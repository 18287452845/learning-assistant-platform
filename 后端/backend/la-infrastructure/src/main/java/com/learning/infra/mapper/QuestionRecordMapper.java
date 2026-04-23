package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.domain.entity.QuestionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 问答记录Mapper接口
 */
@Mapper
public interface QuestionRecordMapper extends BaseMapper<QuestionRecord> {

    /**
     * 分页查询问答记录
     */
    IPage<QuestionRecord> selectQuestionPage(Page<QuestionRecord> page, 
                                              @Param("userId") Long userId,
                                              @Param("courseId") Long courseId);

    /**
     * 查询用户问答统计
     */
    Long countByUserId(@Param("userId") Long userId);
}
