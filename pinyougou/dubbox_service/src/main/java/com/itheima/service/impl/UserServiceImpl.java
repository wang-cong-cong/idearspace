package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.UserService;

/**
 * @author cong
 */
@Service
public class UserServiceImpl implements UserService {


    public String getName(){
        return "itheima";
    }
}
