package com.learning.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询角色列表
     */
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 检查用户名是否存在
     */
    int checkUsernameExists(@Param("username") String username);

    /**
     * 检查手机号是否存在
     */
    int checkPhoneExists(@Param("phone") String phone);

    /**
     * 检查邮箱是否存在
     */
    int checkEmailExists(@Param("email") String email);

    /**
     * 更新登录信息
     */
    int updateLoginInfo(@Param("userId") Long userId, 
                        @Param("ip") String ip);
}
