package com.pinyougou.page.service.impl;


import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;


/**
 * 静态网页生成监听类
 * @author cong
 */

@Component
public class ItemPageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("监听到消息"+goodsIds);
            boolean b = itemPageService.deleteItemHtml(goodsIds);
            System.out.println(b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
