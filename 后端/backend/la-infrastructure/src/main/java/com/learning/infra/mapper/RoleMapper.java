package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色Mapper接口
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色Key查询角色
     */
    Role selectByRoleKey(@Param("roleKey") String roleKey);

    /**
     * 根据角色名称查询角色
     */
    Role selectByRoleName(@Param("roleName") String roleName);
}
