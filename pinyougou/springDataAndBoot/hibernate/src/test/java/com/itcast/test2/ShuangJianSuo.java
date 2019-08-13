package com.itcast.test2;

import javax.persistence.Id;

/**
 * 双重检索单例模式
 * @author cong
 */
public class ShuangJianSuo {
    private ShuangJianSuo(){};

    private volatile static ShuangJianSuo sh;

    public static ShuangJianSuo getSh(){
        if (sh == null){
            synchronized (ShuangJianSuo.class){
                if (sh == null){
                    return sh = new ShuangJianSuo();
                }
            }
        }
        return sh;
    }
}
