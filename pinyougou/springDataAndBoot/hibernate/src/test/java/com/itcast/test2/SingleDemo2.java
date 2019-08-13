package com.itcast.test2;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

/**
 * @author cong
 */
public class SingleDemo2 {

    public static void main(String[] args) {
        Singleton instance = Singleton.INSTANCE;
        System.out.println(instance);

        Singleton instance1 = Singleton.INSTANCE;
        System.out.println(instance1);

    }
}
