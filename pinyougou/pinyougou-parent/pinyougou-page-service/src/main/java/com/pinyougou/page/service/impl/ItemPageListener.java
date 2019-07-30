package com.pinyougou.page.service.impl;


import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * 静态网页生成监听类
 * @author cong
 */

@Component
public class ItemPageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
      TextMessage textMessage = (TextMessage)message;
        try {
            String text = textMessage.getText();
            System.out.println("监听到消息："+text);
            boolean itemHtml = itemPageService.getItemHtml(Long.parseLong(text));
            System.out.println("生成结果"+itemHtml);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
