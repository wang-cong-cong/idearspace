package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

        //关键字中空格处理,替换掉空格
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords",keywords.replace(" ",""));

        //1、查询列表
        map.putAll(searchList(searchMap));

        //2、根据关键字查询商品分类
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);

        //3、根据模板id查询品牌和规格
        String categoryName = (String) searchMap.get("category");

        //如果有分类名称,按照分类名称查品牌和规格
        if (!"".equals(categoryName)){
            map.putAll(searchBrandAndSpecList(categoryName));
        }else {
            //如果没有分类名称，按照第一个商品分类查询品牌和规格
            if (categoryList.size() > 0) {
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }
        return map;
    }

    /**
     * 导入数据
     * @param list
     */
    @Override
    public void importList(List list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    /**
     * 删除数据
     * @param goodsList
     */
    @Override
    public void deleteByGoodsIds(List goodsList) {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(goodsList);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


    /**
     * 获取查询列表
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){
        Map map = new HashMap();
        //构建高亮,设置高亮的前缀和后缀,添加要高亮的域
        HighlightQuery query = new SimpleHighlightQuery();
        HighlightOptions options = new HighlightOptions();
        options.setSimplePrefix("<span style='color:red'>");
        options.setSimplePostfix("</span>");
        options.addField("item_title");
        query.setHighlightOptions(options);

        //1.1关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //1.2按照商品分类过滤
        if (!"".equals(searchMap.get("category"))) {
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.3按照品牌分类过滤
        if (!"".equals(searchMap.get("brand"))) {
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.4按照规格分类过滤
        if (searchMap.get("spec")!=null){
          Map<String,String> specMap = (Map<String, String>) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
                FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //1.5按照价格分类过滤
        if (!"".equals(searchMap.get("price"))){
            String[] price = ((String) searchMap.get("price")).split("-");
            //最低价格
            if (!price[0].equals("0")) {
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
                FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            //最高价格
            if (!price[1].equals("*")) {
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
                FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //1.6按照分页过滤
        Integer pageNo = (Integer) searchMap.get("pageNo");
        if (pageNo == null){
            //默认为第1页
            pageNo=1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageSize == null){
            //默认为20页
            pageSize=20;
        }
        //设置起始索引
        query.setOffset((pageNo-1)*pageSize);
        //设置每页记录数
        query.setRows(pageSize);

        //1.7 按照排序和降序查询
        String sortValue = (String) searchMap.get("sort");
        String sortField = (String) searchMap.get("sortField");
        if (sortValue!=null && !"".equals(sortField)){
            if (sortValue.equals("ASC")){
                Sort sort = new Sort(Sort.Direction.ASC,"item_"+sortField);
                query.addSort(sort);
            }
            if (sortValue.equals("DESC")){
                Sort sort = new Sort(Sort.Direction.DESC,"item_"+sortField);
                query.addSort(sort);
            }
        }


        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //*******************解析高亮，获得高亮集合***********************
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
        //返回总页数
        map.put("totalPages",page.getTotalPages());
        //返回总记录数
        map.put("total",page.getTotalElements());
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

