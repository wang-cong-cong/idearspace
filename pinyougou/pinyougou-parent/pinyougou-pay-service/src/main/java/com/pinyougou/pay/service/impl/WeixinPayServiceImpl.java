package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;
import util.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cong
 */
@Service(timeout = 10000)
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    /**
     * 发送生成二维码的参数
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    @Override
    public Map<String, String> createNative(String out_trade_no, String total_fee) {
        //创建参数
        Map<String,String> map = new HashMap<>();
        map.put("appid",appid);//公众账号id
        map.put("mch_id",partner);//商户号
        map.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        map.put("body","品优购");//商品描述
        map.put("out_trade_no",out_trade_no);//商户订单号
        map.put("total_fee",total_fee);//金额
        map.put("spbill_create_ip","127.0.0.1");//IP
        map.put("notify_url","http://test.itcast.cn");//毁掉地址可以随便写
        map.put("trade_type","NATIVE");//交易类型

        try {
            //生成要发送的xml
            String signedXml = WXPayUtil.generateSignedXml(map, partnerkey);
            System.out.println("请求参数："+signedXml);

            //发送请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(signedXml);
            client.post();

            //响应结果
            String xmlResult = client.getContent();
            System.out.println("响应结果："+xmlResult);

            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(xmlResult);
            Map<String,String> mapResult = new HashMap<>();
            mapResult.put("code_url",xmlToMap.get("code_url"));
            mapResult.put("total_fee",total_fee);
            mapResult.put("out_trade_no",out_trade_no);
            return mapResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * 发送查询支付的状态的消息
     * @param out_trade_no
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        //生成参数
        Map<String,String> map = new HashMap<>();
        map.put("appid",appid);
        map.put("mch_id",partner);
        map.put("out_trade_no",out_trade_no);
        map.put("nonce_str",WXPayUtil.generateNonceStr());

        try {
            //转换成xml
            String mapToXml = WXPayUtil.generateSignedXml(map, partnerkey);

            //发送消息
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(mapToXml);
            httpClient.post();

            //接收消息
            String result = httpClient.getContent();
            Map<String, String> map1 = WXPayUtil.xmlToMap(result);
            System.out.println(map1);

            return map1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
