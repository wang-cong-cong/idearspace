package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;


public interface CartService {

    /**
     * 购物车列表接口
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);

    /**
     * 保存购物车到redis
     * @param cartList
     * @param username
     */
    public void saveCartListToRedis(List<Cart> cartList,String username);

    /**
     * 从redis中获取数据
     * @param username
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);


    /**
     * 合并购物车
     * @param cartList1
     * @param cartList2
     * @return
     */
    public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
}
