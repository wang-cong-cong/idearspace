package com.itheima.ssm.controller;


import com.itheima.ssm.ISysLogService;
import com.itheima.ssm.domain.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/sysLog")
public class SysLogController {

    @Autowired
    private ISysLogService iSysLogService;

    @RequestMapping("/findAll.do")
    public ModelAndView findAll(){
        ModelAndView mvc = new ModelAndView();
        List<SysLog> all = iSysLogService.findAll();
        mvc.setViewName("syslog-list");
        mvc.addObject("sysLogs",all);
        return mvc;
    }
}
