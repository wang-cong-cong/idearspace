package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IUserDao {

    public UserInfo findByUsername(String username);

    List<UserInfo> findAll();

    void save(UserInfo userInfo);

    UserInfo findById(String id);

    List<Role> findUserByIdAndAllRole(String id);

    void addRoleToUser(@Param("userId") String userId,@Param("roleId") String roleId);
}
