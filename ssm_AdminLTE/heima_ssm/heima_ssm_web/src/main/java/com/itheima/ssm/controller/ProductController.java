package com.itheima.ssm.controller;


import com.itheima.ssm.IProductService;
import com.itheima.ssm.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;


    @RequestMapping("/save.do")
    public String save(Product product){
        iProductService.save(product);
        return "redirect:findAll.do";
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/findAll.do")
    //jsr250注解在控制器类中的方法上控制
//    @RolesAllowed("ADMIN")
//    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")//hasAnyRole可以多个参数,hasRole一个参数
    public ModelAndView findAll(){
        ModelAndView mvc = new ModelAndView();
        List<Product> productList = iProductService.findAll();
        mvc.addObject("productList",productList);
        mvc.setViewName("productList");
        return mvc;
    }
}
