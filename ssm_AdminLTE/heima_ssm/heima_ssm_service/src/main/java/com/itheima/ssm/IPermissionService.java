package com.itheima.ssm;

import com.itheima.ssm.domain.Permission;

import java.util.List;

public interface IPermissionService {

    public List<Permission> findAll(int page,int size);

    void save(Permission permission);
}
