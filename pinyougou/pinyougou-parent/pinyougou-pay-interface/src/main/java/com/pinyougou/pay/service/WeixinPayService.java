package com.pinyougou.pay.service;

import java.util.Map;

/**
 * 生成二维码的接口
 * @author cong
 */
public interface WeixinPayService {

    /**
     * 发送统一下单，发送生成二维码的消息
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    public Map<String,String> createNative(String out_trade_no,String total_fee);


    /**
     * 根据商品订单号查询支付状态
     * @param out_trade_no
     * @return
     */
    public Map<String,String> queryPayStatus(String out_trade_no);
}
