package com.itheima.springboottest.mapper;

import com.itheima.springboottest.domin.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cong
 */
@Mapper
public interface UserMapper {

    public List<User> findAll();
}
