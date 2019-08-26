package com.itheima.springboottest;

/**
 * 如何校验对象被回收
 * @author cong
 */
public class ObjectTest {
    public static void main(String[] args) {

        for (int i = 1; i <50000; i++) {
            new demo();
        }


    }

    static class demo extends Object{
        @Override
        protected void finalize() throws Throwable {
            System.out.println("垃圾被清扫了");
        }
    }

}
