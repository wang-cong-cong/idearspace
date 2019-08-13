package com.itcast.test2;

/**
 * 单利模式中的：饿汉式
 * @author cong
 */
public class SingleDemo {

    //私有化无参构造后无法创造对象
    private SingleDemo(){
        if (instence!=null){
            throw  new RuntimeException("非法操作");
        }
    };


    public static SingleDemo instence = new SingleDemo();


}
