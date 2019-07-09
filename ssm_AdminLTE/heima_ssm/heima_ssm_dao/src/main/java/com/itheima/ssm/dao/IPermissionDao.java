package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Permission;

import java.util.List;

public interface IPermissionDao {

    public List<Permission> findAll();

    void save(Permission permission);

}
