package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.dao.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;

/**
 * @author cong
 */

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 导入商品到solr索引库
     */
    public void importItemData(){

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //条件是查出已审核的商品
        criteria.andStatusEqualTo("1");
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        for (TbItem tbItem : tbItems) {
            //将spec字段中的json字符串转换为map
            Map specMap = JSON.parseObject(tbItem.getSpec());
            //给带注解的字段赋值
            tbItem.setSpecMap(specMap);
            System.out.println(tbItem.getTitle());
        }

        solrTemplate.saveBeans(tbItems);
        solrTemplate.commit();
    }

    /**
     * 删除数据
     */
    public void deleteAll(){
        SolrDataQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
