package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.domain.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 资源Mapper接口
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 分页查询资源
     */
    IPage<Resource> selectResourcePage(Page<Resource> page, 
                                       @Param("courseId") Long courseId,
                                       @Param("status") Integer status,
                                       @Param("uploaderId") Long uploaderId);

    /**
     * 查询待审核资源列表
     */
    IPage<Resource> selectPendingResources(Page<Resource> page);

    /**
     * 更新下载次数
     */
    int updateDownloadCount(@Param("resourceId") Long resourceId);
}
