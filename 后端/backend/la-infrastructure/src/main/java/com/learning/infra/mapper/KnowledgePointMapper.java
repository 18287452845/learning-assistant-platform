package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.domain.entity.KnowledgePoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识点Mapper接口
 */
@Mapper
public interface KnowledgePointMapper extends BaseMapper<KnowledgePoint> {

    /**
     * 根据课程ID查询知识点列表
     */
    List<KnowledgePoint> selectByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据父ID查询子知识点
     */
    List<KnowledgePoint> selectByParentId(@Param("parentId") Long parentId);
}
