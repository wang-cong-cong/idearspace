package com.itheima.ssm;

import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;

import java.util.List;

public interface IRoleService {

    public List<Role> findAll();

    void save(Role role);

    Role findById(String roleId);

    List<Permission> findRoleByIdAndAllPermission(String id);

    void addPermissionToRole(String roleId, String[] permissionIds);
}
