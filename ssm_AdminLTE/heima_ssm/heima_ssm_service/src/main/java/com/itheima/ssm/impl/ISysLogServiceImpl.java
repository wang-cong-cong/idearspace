package com.itheima.ssm.impl;

import com.itheima.ssm.ISysLogService;
import com.itheima.ssm.dao.ISysLogDao;
import com.itheima.ssm.domain.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sysLogService")
public class ISysLogServiceImpl implements ISysLogService {

    @Autowired
    private ISysLogDao iSysLogDao;

    @Override
    public void save(SysLog sysLog) {
       iSysLogDao.save(sysLog);
    }

    @Override
    public List<SysLog> findAll() {
       return iSysLogDao.findAll();
    }
}
