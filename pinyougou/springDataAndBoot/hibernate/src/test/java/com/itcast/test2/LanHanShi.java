package com.itcast.test2;

/**
 * 懒汉式线程不安全，要安全必须加锁
 * @author cong
 */
public class LanHanShi {

    private LanHanShi(){};

    private static LanHanShi lanHanShi ;

    public static synchronized LanHanShi getLanHanShi(){

            if (lanHanShi == null) {
                lanHanShi = new LanHanShi();
            }
        return lanHanShi;
    }
}
