package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cong
 */
@RestController
@RequestMapping("/cart")
public class CartListController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        //获取当前登录的用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前用户"+username);

        String cartListString = CookieUtil.getCookieValue(request, "cartList", "utf-8");
        if (cartListString==null || "".equals(cartListString) ){
            cartListString = "[]";
        }
        //转换JSON字符串为集合
        List<Cart> carts = JSON.parseArray(cartListString, Cart.class);
        if (username.equals("anonymousUser")){
         //未登录
            //从cookie中获取购物车
            System.out.println("未登录从cookie中获取购物车");
            return carts;
        }else{
            //已登录从redis获取
            System.out.println("已登录从redis获取");
            List<Cart> cartListFromRedis = cartService.findCartListFromRedis(username);
            //如果缓存中的购物车有数据时再进行合并购物车
            if (carts.size()>0) {
                //合并缓存和redis中的购物车
                List<Cart> mergeCartList = cartService.mergeCartList(carts, cartListFromRedis);
                //将合并后的购物车列表存入redis
                cartService.saveCartListToRedis(mergeCartList, username);
                //清除缓存中的cookie
                CookieUtil.deleteCookie(request, response, "cartList");
                return mergeCartList;
            }
            return cartListFromRedis;
        }
    }

    @RequestMapping("/addGoodToCart")
    //@CrossOrigin(origins = "http://localhost:9105",allowCredentials = "true")
    public Result addGoodToCart(Long itemId,Integer num){

        //设置可以访问域的响应头
        response.setHeader("Access-Control-Allow-Origin","http://localhost:9105");
        //设置客户端可以携带cookie
        response.setHeader("Access-Control-Allow-Credentials","true");

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前用户"+name);
        try {
            //从cookie中提取购物车
            List<Cart> cartList = findCartList();
            //调用服务层方法添加购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);


            if (name.equals("anonymousUser")){
                //未登录
                //将新的购物车存入cookie中
                System.out.println("未登录存入cookie");
                String jsonString = JSON.toJSONString(cartList);
                CookieUtil.setCookie(request,response,"cartList",jsonString,3600*24,"utf-8") ;
            }else{
                System.out.println("已登录存入redis");
                //已登录向redis添加
                cartService.saveCartListToRedis(cartList,name);

            }
            return new Result(true,"添加购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"添加购物车失败");
        }
    }
}
