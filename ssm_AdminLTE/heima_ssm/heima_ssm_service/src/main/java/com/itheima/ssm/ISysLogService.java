package com.itheima.ssm;

import com.itheima.ssm.domain.SysLog;

import java.util.List;

/**
 *日志service层
 * @author Administrator
 */
public interface ISysLogService {


    /**
     *
     * 保存日志
     * @param sysLog
     */
    void save(SysLog sysLog);

    /**
     * 查询日志
     * @return
     */
    List<SysLog> findAll();

}
