package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.dao.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryExplorerTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cong
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {

        //1、根据skuId查询出商品明细sku的对象
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null){
            throw  new RuntimeException("该商品不存在");
        }
        if (!"1".equals(item.getStatus())){
            throw new RuntimeException("你要购买的商品已经取消");
        }
        //2、根据sku的对象查询出商家Id
        String sellerId = item.getSellerId();
        //3、根据商家Id在购物车列表中查出购物车对象
        Cart cart = searchCartBySellerId(cartList, sellerId);
        if (cart == null){
            //4、 如果购物车列表中不存在该商家购物车对象
            //4、1创建一个新的购物车
             cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            List<TbOrderItem> orderItemList = new ArrayList<>();
            TbOrderItem orderItem = getTbOrderItemByItem(item, num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //4、2将新的购物车对象添加到购物车列表中
            cartList.add(cart);
        }else{
            //5 如果购物车列表中存在该商家购物车对象
            //判断该商品是否在该商家购物车的明细列表中
            TbOrderItem tbOrderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            if (tbOrderItem == null){
                //5、1如果不存在将该商品添加到购物车的明细表中
                TbOrderItem tbOrderItemByitem = getTbOrderItemByItem(item, num);
                cart.getOrderItemList().add(tbOrderItemByitem);
            }else{
                //5、2如果存在将改变该商品的数量，金额数
                //更改数量
                tbOrderItem.setNum(tbOrderItem.getNum()+num);
                tbOrderItem.setTotalFee(new BigDecimal(tbOrderItem.getPrice().doubleValue()*tbOrderItem.getNum()));
                //当商品数量小于等于0时，从购物车明细列表中移除该商品
                if (tbOrderItem.getNum()<=0){
                    cart.getOrderItemList().remove(tbOrderItem);
                }
                //当购物从明细列表等于0时从购物车列表移除该购物车
                if (cart.getOrderItemList().size()==0){
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }

    /**
     *向redis中存储购物车
     * @param cartList
     * @param username
     */
    @Override
    public void saveCartListToRedis(List<Cart> cartList, String username) {
        System.out.println("存数据："+username);
        redisTemplate.boundHashOps("cartList").put(username,cartList);
    }

    /**
     * 从redis中获取购物车
     * @param username
     * @return
     */
    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("取数据："+username);
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList == null){
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    /**
     * 合并购物车
     * @param cartList1
     * @param cartList2
     * @return
     */
    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        for (Cart cart : cartList2) {
            for (TbOrderItem tbOrderItem : cart.getOrderItemList()) {
                 cartList1 = addGoodsToCartList(cartList1, tbOrderItem.getItemId(), tbOrderItem.getNum());
            }
        }
        return cartList1;
    }

    /**
     * 根据商品Id查询商品明细
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList,Long itemId){
        for (TbOrderItem tbOrderItem : orderItemList) {
            if (tbOrderItem.getItemId().longValue() == itemId.longValue()){
                return tbOrderItem;
            }
        }
        return null;
    }



    /**
     * 根据商家ID在购物车列表中查出该商家的购物车
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    /**
     * 创建购物车明细对象
     * @param tbItem
     * @param num
     * @return
     */
    private TbOrderItem getTbOrderItemByItem(TbItem tbItem,Integer num){
        TbOrderItem tbOrderItem = new TbOrderItem();
        tbOrderItem.setGoodsId(tbItem.getGoodsId());
        tbOrderItem.setItemId(tbItem.getId());
        tbOrderItem.setNum(num);
        tbOrderItem.setPicPath(tbItem.getImage());
        tbOrderItem.setPrice(tbItem.getPrice());
        tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue()*num));
        tbOrderItem.setTitle(tbItem.getTitle());
        return tbOrderItem;
    }
}
