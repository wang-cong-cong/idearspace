package com.pinyougou.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cong
 */
@RestController
@RequestMapping("/login")
public class LoginController {


    @RequestMapping("/name.do")
    public Map getName(){

        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        System.out.println(name);

        Map map = new HashMap();
        map.put("loginName",name);
        return map;
    }
}
