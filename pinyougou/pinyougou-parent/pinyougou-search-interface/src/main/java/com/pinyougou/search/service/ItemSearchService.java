package com.pinyougou.search.service;

import java.util.Map;

/**
 * @author cong
 */
public interface ItemSearchService {


    /**
     * 搜索方法
     * @param searchMap
     * @return
     */
    public Map<String,Object> search(Map searchMap);
}
