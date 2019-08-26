package com.itcast.test4;

/**
 * @author cong
 */
public class RunnableImpl implements Runnable {

    private int ticket = 100;

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this) {
                if (ticket <= 0) {
                    break;
                }
                String name = Thread.currentThread().getName();
                System.out.println(name+"正在卖出第"+(100-ticket+1)+"张票。还剩余"+(--ticket)+"张");
            }
        }

    }
}
