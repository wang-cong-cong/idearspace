package com.itheima.springboottest.controller;

import com.itheima.springboottest.domin.User;
import com.itheima.springboottest.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cong
 */
@RestController
public class QuickStartController2 {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/quick2")
    public List<User> quick2(){
        return userMapper.findAll();
    }
}
