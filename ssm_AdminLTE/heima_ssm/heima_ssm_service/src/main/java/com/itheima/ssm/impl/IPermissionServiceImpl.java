package com.itheima.ssm.impl;

import com.github.pagehelper.PageHelper;
import com.itheima.ssm.IPermissionService;
import com.itheima.ssm.dao.IPermissionDao;
import com.itheima.ssm.domain.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("permissionService")
public class IPermissionServiceImpl implements IPermissionService {

    @Autowired
    private IPermissionDao iPermissionDao;

    @Override
    public List<Permission> findAll(int page,int size) {
        PageHelper.startPage(page,size);
        return iPermissionDao.findAll();
    }

    @Override
    public void save(Permission permission) {
        iPermissionDao.save(permission);
    }
}
