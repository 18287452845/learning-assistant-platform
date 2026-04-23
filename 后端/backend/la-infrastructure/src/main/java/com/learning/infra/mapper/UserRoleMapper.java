package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.domain.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色Mapper接口
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
