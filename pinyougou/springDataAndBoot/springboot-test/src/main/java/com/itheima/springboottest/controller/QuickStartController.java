package com.itheima.springboottest.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cong
 */
@RestController
@ConfigurationProperties(prefix = "person")
public class QuickStartController {

    private String name;
    private String addr;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @RequestMapping("/quick")
    public String quick(){
        return "被访问"+name+age+addr;
    }
}
