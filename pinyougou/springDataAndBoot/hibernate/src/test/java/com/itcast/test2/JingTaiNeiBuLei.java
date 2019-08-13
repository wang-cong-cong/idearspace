package com.itcast.test2;

import java.util.function.Predicate;

/**
 * 静态内部类
 */
public class JingTaiNeiBuLei {

    private JingTaiNeiBuLei(){};

    private static class sing{
        private final static JingTaiNeiBuLei j1= new JingTaiNeiBuLei();
    }

    public static JingTaiNeiBuLei getSing(){
        return sing.j1;
    }
}
