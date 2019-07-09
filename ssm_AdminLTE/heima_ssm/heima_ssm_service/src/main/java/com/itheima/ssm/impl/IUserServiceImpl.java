package com.itheima.ssm.impl;

import com.itheima.ssm.IUserService;
import com.itheima.ssm.dao.IUserDao;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import com.itheima.ssm.utils.BCryptPasswordEncoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class IUserServiceImpl implements IUserService {

    @Autowired
    private IUserDao iUserDao;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = iUserDao.findByUsername(username);
        if (userInfo==null){
            throw new UsernameNotFoundException("["+username+"]未找到");
        }

       // User user = new User(userInfo.getUsername(),"{noop}"+userInfo.getPassword(),getAuthority(userInfo.getRoles()));
        User user = new User(userInfo.getUsername(),userInfo.getPassword(),userInfo.getStatus()==0 ? false:true,true,true,true,getAuthority(userInfo.getRoles()));
        return user;
    }

    //Collection< GrantedAuthority> authorities
    private List<SimpleGrantedAuthority> getAuthority(List<Role> roles) {

        List<SimpleGrantedAuthority> arrayList = new ArrayList<>();
        for (Role role : roles) {
            arrayList.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        }
        return arrayList;
    }


    @Override
    public List<UserInfo> findAll() {
        return iUserDao.findAll();
    }


    /**
     * 保存时把密码加密
     * @param userInfo
     */
    @Override
    public void save(UserInfo userInfo) {
//       userInfo.setPassword(BCryptPasswordEncoderUtils.encodePassword(userInfo.getPassword()));
        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        iUserDao.save(userInfo);
    }


    /**
     * 通过用户 查询角色，权限
     * @param id
     * @return
     */
    @Override
    public UserInfo findById(String id) {
        return iUserDao.findById(id);
    }

    /**
     * 根据用户id查询用户没有的角色
     * @param id
     * @return
     */
    @Override
    public List<Role> findUserByIdAndAllRole(String id) {
        return iUserDao.findUserByIdAndAllRole(id);
    }

    @Override
    public void addRoleToUser(String userId, String[] roleIds) {
        for (String roleId : roleIds) {
            iUserDao.addRoleToUser(userId,roleId);
        }
    }
}
