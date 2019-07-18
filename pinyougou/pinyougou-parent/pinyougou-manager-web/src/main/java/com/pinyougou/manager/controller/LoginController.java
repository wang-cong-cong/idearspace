package com.pinyougou.manager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cong
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name.do")
    public Map getLoginName(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String name = authentication.getName();
        Map  map = new HashMap();
        map.put("loginName",name);
        return map;
    }
}
