package com.pinyougou.page.service;

/**
 * 商品详细页接口
 * @author cong
 */
public interface ItemPageService {

    /**
     * 生成静态网页
     * @param goodsId
     * @return
     */
    public boolean getItemHtml(Long goodsId);

    /**
     * 删除静态网页
     * @param goodsId
     * @return
     */
    public boolean deleteItemHtml(Long[] goodsId);
}
