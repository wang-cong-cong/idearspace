package com.pinyougo.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.dao.TbGoodsDescMapper;
import com.pinyougou.dao.TbGoodsMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cong
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Value("${pagedir}")
    private String pagedir;

    @Override
    public boolean getItemHtml(Long goodsId) {
        //获取配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            //获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            Map dataModel = new HashMap();
            //1、获取商品表数据
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goods",goods);
            //2、获取商品扩展表数据
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goodsDesc",goodsDesc);
            //创建writer对象
            Writer out = new FileWriter(pagedir+goodsId+".html");
            template.process(dataModel,out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
