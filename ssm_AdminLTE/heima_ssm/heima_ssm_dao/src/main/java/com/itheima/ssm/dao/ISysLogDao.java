package com.itheima.ssm.dao;

import com.itheima.ssm.domain.SysLog;

import java.util.List;

/**
 * @author Administrator
 */
public interface ISysLogDao {

    /**
     * 保存日志
     * @param sysLog
     */
    void  save(SysLog sysLog);

    /**
     * 查询日志
     * @return
     */
    List<SysLog> findAll();
}
