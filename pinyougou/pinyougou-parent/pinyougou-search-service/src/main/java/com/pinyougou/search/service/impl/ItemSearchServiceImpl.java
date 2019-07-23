package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {

        Map map = new HashMap();
        //1、查询列表
        map.putAll(searchList(searchMap));

        //2、根据关键字查询商品分类
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);

        //3、根据模板id查询品牌和规格
        if (categoryList.size()>0){
            map.putAll(searchBrandAndSpecList(categoryList.get(0)));
        }
        return map;
    }


    /**
     * 获取查询列表
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){
        Map map = new HashMap();
        //构建高亮
        HighlightQuery query = new SimpleHighlightQuery();
        HighlightOptions options = new HighlightOptions();
        //设置高亮的前缀和后缀
        options.setSimplePrefix("<span style='color:red'>");
        options.setSimplePostfix("</span>");
        //添加要高亮的域
        options.addField("item_title");
        query.setHighlightOptions(options);

        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //获得高亮，并解析高亮
        List<HighlightEntry<TbItem>> highlightEntryList = page.getHighlighted();
        //遍历集合获得所有高亮域
        for (HighlightEntry<TbItem> entry : highlightEntryList) {
            TbItem entity = entry.getEntity();
            List<HighlightEntry.Highlight> highlights = entry.getHighlights();
            //获取其中第一个高亮域的第一个内容
            if (highlights.size()>0&&highlights.get(0).getSnipplets().size()>0) {
                String s = highlights.get(0).getSnipplets().get(0);
                entity.setTitle(s);
            }
        }

        map.put("rows",page.getContent());
        return map;
    }


    /**
     * 查询分类列表
     */
    private List searchCategoryList(Map searchMap){
        List list = new ArrayList();

        Query query = new SimpleQuery("*:*");

        //设置分组选项
        GroupOptions options = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(options);


        //根据关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //获取分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);

        //根据列获得分组结果集合
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");

        //获得分组结果入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();

        //获得分组入口集合
        List<GroupEntry<TbItem>> content = groupEntries.getContent();

        for (GroupEntry<TbItem> tbItemGroupEntry : content) {
            //将分组名称集合添加到集合
            list.add(tbItemGroupEntry.getGroupValue());
        }


        return list;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据模板id查询品牌和规格列表
     */
    private Map searchBrandAndSpecList(String category){

        Map map = new HashMap();

        //获取模板id
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);

        if(typeId != null){
            //根据模板ID查询品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            System.out.println(brandList);

            map.put("brandList",brandList);

            //根据模板ID查询规格列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            System.out.println(specList);

            map.put("specList",specList);

        }
        return map;
    }


}

