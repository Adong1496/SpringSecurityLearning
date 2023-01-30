package com.hjd.service.impl;

import com.hjd.domain.LoginUser;
import com.hjd.domain.ResponseResult;
import com.hjd.domain.User;
import com.hjd.service.LoginService;
import com.hjd.utils.JwtUtil;
import com.hjd.utils.RedisCache;
import jdk.internal.org.objectweb.asm.Handle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //1.对用户进行认证
        //1.1 先将用户名和密码封装到认证对象中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //1.2 认证对象被认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //2.若用户认证失败，抛出异常，提示错误信息
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //3，若用户认证成功，解析该用户，获取用户id
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //4.生成jwt
        String jwt = JwtUtil.createJWT(userId);
        //5.将用户信息信息存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //6.将token响应给前端
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登录成功",map);
    }

    @Override
    public ResponseResult logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        //将用户信息 从redis中删除
        redisCache.deleteObject("login:" + userId);
        return new ResponseResult(200,"注销成功");
    }
}
