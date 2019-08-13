package cn.itcast.test;

import java.text.Collator;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cong
 */
public class Test {

    public static void main(String[] args) {
        ArrayList arrayList = new ArrayList();
        List list = Collections.synchronizedList(arrayList);

    }
}
