package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;

import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cong
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;

    @RequestMapping("/createNative")
    public Map<String,String> createNative(){
        //从安全认证中获取用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据用户名从缓存中提取日志
        TbPayLog tbPayLog = orderService.searchPaylogFromRedis(userName);
        if (tbPayLog!=null){
            return  weixinPayService.createNative(tbPayLog.getOutTradeNo(), tbPayLog.getTotalFee().toString());
        }else{
            return new HashMap<>();
        }
    }

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        long start = System.currentTimeMillis();
        while (true){
            Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);
            if (map == null){
                result =  new Result(false,"支付失败");
                break;
            }
            if ("SUCCESS".equals(map.get("trade_state"))){
                result =  new Result(true,"支付成功");

                orderService.updateOrderStatus(out_trade_no,map.get("transaction_id"));

                break;
            }
            try {
                Thread.sleep(3000);
                System.out.println("检查状态");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long end = System.currentTimeMillis();
            if ((end-start)>=50000){
                result = new Result(false,"二维码超时");
                break;
            }
        }
        return result;
    }
}
