package com.itheima.ssm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.ssm.IOrdersService;
import com.itheima.ssm.domain.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private IOrdersService iOrdersService;
//    未分页
//    @RequestMapping("findAll.do")
//    public ModelAndView findAll(){
//        ModelAndView mvc = new ModelAndView();
//        List<Orders> all = iOrdersService.findAll();
//        mvc.setViewName("orders-list");
//        mvc.addObject("ordersList",all);
//        return mvc;
//    }

    //分页后
    @RequestMapping("/findAll.do")
    public ModelAndView findAll(@RequestParam(name = "page",required = true,defaultValue = "1")int page,@RequestParam(name = "size",defaultValue = "4",required = true)int size){
        ModelAndView mvc = new ModelAndView();
        List<Orders> all = iOrdersService.findAll(page,size);
        PageInfo pageInfo = new PageInfo(all);
        mvc.setViewName("orders-page-list");
        mvc.addObject("pageInfo",pageInfo);
        return mvc;
    }

    @RequestMapping("/findById.do")
    public ModelAndView findById(@RequestParam(name = "id",required = true)String ordersId){
        ModelAndView mvc = new ModelAndView();
        Orders orders =  iOrdersService.findById(ordersId);
        mvc.addObject("orders",orders);
        mvc.setViewName("orders-show");
        return mvc;
    }

}
