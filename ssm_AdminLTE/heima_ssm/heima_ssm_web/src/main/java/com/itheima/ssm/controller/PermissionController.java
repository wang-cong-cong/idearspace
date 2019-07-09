package com.itheima.ssm.controller;


import com.github.pagehelper.PageInfo;
import com.itheima.ssm.IPermissionService;
import com.itheima.ssm.domain.Permission;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private IPermissionService iPermissionService;


    /**
     * 未分页
     * @return
     */
//    @RequestMapping("/findAll.do")
//    public ModelAndView findAll(){
//        ModelAndView mvc = new ModelAndView();
//        List<Permission> all = iPermissionService.findAll();
//        mvc.addObject("permissionList",all);
//        mvc.setViewName("permission-list");
//        return mvc;
//    }

    /**
     * 分页展示
     * @param
     * @return
     */
    @RequestMapping("/findAll.do")
    public ModelAndView findAll(@RequestParam(name = "page",defaultValue = "1",required = true)int page,@RequestParam(name = "size",defaultValue = "4",required = true)int size){
        ModelAndView mvc = new ModelAndView();
        List<Permission> all = iPermissionService.findAll(page,size);
        PageInfo pageInfo = new PageInfo(all);
        mvc.addObject("pageInfo",pageInfo);
        mvc.setViewName("permission-page-list");
        return mvc;
    }


    /**
     * 保存信息
     * @param permission
     * @return
     */
    @RequestMapping("/save.do")
    public String save(Permission permission){
        iPermissionService.save(permission);
        return "redirect:findAll.do";
    }
}
