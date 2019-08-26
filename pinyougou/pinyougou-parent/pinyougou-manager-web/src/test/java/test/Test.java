package test;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author cong
 */
public class Test {
    public static void main(String[] args) {

        for (int i = 0; i <100 ; i++) {

            String s = RandomStringUtils.randomNumeric(6);
            System.out.println(s);
        }
    }
}
