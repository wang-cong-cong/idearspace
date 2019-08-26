package com.itcast.test2;

import java.util.function.Predicate;

/**
 * 静态内部类
 */
public class JingTaiNeiBuLei {
    private JingTaiNeiBuLei(){};

    private static class jingtai{
        private static final JingTaiNeiBuLei js = new JingTaiNeiBuLei();
    }

    public static final JingTaiNeiBuLei getInstance(){
        return jingtai.js;
    }
}
