package com.hjd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjd.domain.LoginUser;
import com.hjd.domain.User;
import com.hjd.mapper.MenuMapper;
import com.hjd.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名来查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        //如果用户不存在，返回异常信息
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //根据用户查询权限信息 添加到LoginUser中
        //List<String> list = new ArrayList<>(Arrays.asList("test","admin"));
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        //将数据封装成UserDetails对象返回
        return new LoginUser(user,list);
    }
}
