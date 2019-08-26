package com.itcast.test2;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.hamcrest.core.IsNot;

/**
 * @author cong
 */
public class SingleDemo2 {

    public static void main(String[] args) {

        MeiJuTest instance = MeiJuTest.INSTANCE;
        System.out.println(instance);
        MeiJuTest instance1 = MeiJuTest.INSTANCE;
        System.out.println(instance1);
    }
}
