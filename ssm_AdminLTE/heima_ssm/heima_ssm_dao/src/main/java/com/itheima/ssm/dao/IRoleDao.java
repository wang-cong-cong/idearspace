package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface IRoleDao {

    List<Role> findAll();

    void save(Role role);

    Role findById(String roleId);

    List<Permission> findRoleByIdAndAllPermission(String id);

    void addPermissionToRole(@Param("roleId")String roleId,@Param("permissionId")String permissionId);
}
