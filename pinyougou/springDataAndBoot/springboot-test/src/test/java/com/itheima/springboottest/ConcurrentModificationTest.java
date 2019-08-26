package com.itheima.springboottest;

import java.util.*;

/**
 * 并发修改异常
 * @author cong
 */
public class ConcurrentModificationTest {

    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();

        LinkedList<String> linkedList = new LinkedList<String>();
        list.add("aaaaa");
        list.add("bbbbb");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if ("bbbbb".equals(next)){
                iterator.remove();
            }
            System.out.println(next);
        }

        Collections.reverse(linkedList);

    }
}
