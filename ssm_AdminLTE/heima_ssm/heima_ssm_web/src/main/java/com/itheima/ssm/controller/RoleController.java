package com.itheima.ssm.controller;


import com.itheima.ssm.IRoleService;
import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;



@RequestMapping("/role")
@Controller
public class RoleController {

    @Autowired
    private IRoleService iRoleService;


    @RequestMapping("/addPermissionToRole.do")
    public String addPermissionToRole(@RequestParam(name = "roleId",required = true)String roleId,@RequestParam(name = "ids",required = true)String[] permissionIds){
        iRoleService.addPermissionToRole(roleId,permissionIds);
        return "redirect:findAll.do";
    }


    //根据roleid查询role，并查询出可以添加的权限
    @RequestMapping("/findRoleByIdAndAllPermission.do")
    public ModelAndView findRoleByIdAndAllPermission(String id) {
        ModelAndView mvc = new ModelAndView();
        Role role = iRoleService.findById(id);
        List<Permission> permissionList = iRoleService.findRoleByIdAndAllPermission(id);
        mvc.addObject("role", role);
        mvc.addObject("permissionList", permissionList);
        mvc.setViewName("role-permission-add");
        return mvc;
    }


    @RequestMapping("/findAll.do")
    public ModelAndView findAll() {
        ModelAndView mvc = new ModelAndView();
        List<Role> all = iRoleService.findAll();
        mvc.setViewName("role-list");
        mvc.addObject("roleList", all);
        return mvc;
    }

    @RequestMapping("/save.do")
    public String save(Role role) {
        iRoleService.save(role);
        return "redirect:findAll.do";
    }
}
