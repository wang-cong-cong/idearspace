package com.itheima.ssm.controller;

import com.itheima.ssm.IUserService;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping("/findAll.do")
    public ModelAndView findAll(){
        ModelAndView mvc = new ModelAndView();
        List<UserInfo> all = iUserService.findAll();
        mvc.setViewName("user-list");
        mvc.addObject("userList",all);
        return mvc;
    }


    @RequestMapping("/save.do")
    public String save(UserInfo userInfo){
        iUserService.save(userInfo);
        return "redirect:findAll.do";
    }

    @RequestMapping("/findById.do")
    public ModelAndView findById(String id){
        ModelAndView mvc = new ModelAndView();
        UserInfo user =  iUserService.findById(id);
        mvc.addObject("user",user);
        mvc.setViewName("user-show");
        return mvc;
    }

    /**
     * 查询用户除了自身的角色外其他的条件
     */
    @RequestMapping("/findUserByIdAndAllRole.do")
    public ModelAndView findUserByIdAndAllRole(String id){
        ModelAndView mvc = new ModelAndView();
        UserInfo byId = iUserService.findById(id);
        List<Role> role = iUserService.findUserByIdAndAllRole(id);
        mvc.addObject("user",byId);
        mvc.addObject("roleList",role);
        mvc.setViewName("user-role-add");
        return mvc;
    }

    /**
     * 给用户添加角色
     */
    @RequestMapping("/addRoleToUser.do")
    public String addRoleToUser(@RequestParam(name = "userId",required = true)String userId,@RequestParam(name = "ids",required = true)String[] roleIds){
        iUserService.addRoleToUser(userId,roleIds);
        return "redirect:findAll.do";
    }
}
