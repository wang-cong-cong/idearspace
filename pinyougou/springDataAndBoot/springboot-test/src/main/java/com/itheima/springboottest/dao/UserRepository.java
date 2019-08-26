package com.itheima.springboottest.dao;

import com.itheima.springboottest.domin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author cong
 */
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
}
