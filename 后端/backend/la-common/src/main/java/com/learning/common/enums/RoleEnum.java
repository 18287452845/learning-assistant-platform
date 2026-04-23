package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    STUDENT(1L, "STUDENT", "学生"),
    TEACHER(2L, "TEACHER", "教师"),
    ADMIN(3L, "ADMIN", "管理员");

    private final Long id;
    private final String roleKey;
    private final String roleName;

    /**
     * 根据角色Key获取枚举
     */
    public static RoleEnum getByRoleKey(String roleKey) {
        for (RoleEnum roleEnum : values()) {
            if (roleEnum.getRoleKey().equalsIgnoreCase(roleKey)) {
                return roleEnum;
            }
        }
        return null;
    }

    /**
     * 根据角色ID获取枚举
     */
    public static RoleEnum getById(Long id) {
        for (RoleEnum roleEnum : values()) {
            if (roleEnum.getId().equals(id)) {
                return roleEnum;
            }
        }
        return null;
    }
}
