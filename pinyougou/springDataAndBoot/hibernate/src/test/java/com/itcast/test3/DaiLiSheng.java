package com.itcast.test3;

/**
 * @author cong
 */
public class DaiLiSheng implements Levneo {
    @Override
    public String sale(double money) {
        System.out.println("花了"+money+"买电脑");
        return "联想电脑";
    }
}
