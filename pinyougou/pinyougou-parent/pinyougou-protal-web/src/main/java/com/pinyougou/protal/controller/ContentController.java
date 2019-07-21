package com.pinyougou.protal.controller;

import com.pinyougou.content.service.ContentService;
import com.pinyougou.pojo.TbContent;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cong
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    /**
     * 根据广告Id查询列表
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategoryId.do")
    public List<TbContent>  findByCategoryId(Long categoryId){
        return  contentService.findByCategoryId(categoryId);
    }
}
