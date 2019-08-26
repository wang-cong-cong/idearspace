package com.itcast.test4;

/**
 * @author cong
 */
public class RunnableImplTest {
    public static void main(String[] args) {
        RunnableImpl runnable = new RunnableImpl();
        new Thread(runnable,"窗口1").start();
        new Thread(runnable,"窗口2").start();
    }
}
