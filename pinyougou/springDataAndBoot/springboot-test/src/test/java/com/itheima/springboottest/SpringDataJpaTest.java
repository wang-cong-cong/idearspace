package com.itheima.springboottest;

import com.itheima.springboottest.dao.UserRepository;
import com.itheima.springboottest.domin.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author cong
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootTestApplication.class)
public class SpringDataJpaTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void  test1(){
        List<User> all = userRepository.findAll();
        System.out.println(all);
    }
}
