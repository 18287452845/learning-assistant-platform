package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
