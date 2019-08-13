package com.itcast.test3;

import javax.lang.model.element.VariableElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 *
 * 动态代理
 * @author cong
 */
public class ProxyTest {
    public static void main(String[] args) {
        //真实对象
        DaiLiSheng daiLiSheng = new DaiLiSheng();

        //代理对象
        Levneo proxy_Dai = (Levneo) Proxy.newProxyInstance(daiLiSheng.getClass().getClassLoader(), daiLiSheng.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("sale".equals(method.getName())){
                    double arg = (double) args[0];
                    double money = arg* 0.85;
                    String invoke = (String) method.invoke(daiLiSheng, money);
                    return invoke;
                }else{

                    return method.invoke(daiLiSheng,args);
                }

            }
        });
        String sale = proxy_Dai.sale(8000);
        System.out.println(sale);
    }
}
