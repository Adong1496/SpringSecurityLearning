package com.hjd.filter;

import com.hjd.domain.LoginUser;
import com.hjd.utils.JwtUtil;
import com.hjd.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //1.从请求头中获取token
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            //2.如果token为空，直接放行，让后续的过滤器进行处理
            filterChain.doFilter(request, response);
            return;
        }
        //3.如果token不为空
        String userId;
        try {
            //3.1 解析token
            Claims claims = JwtUtil.parseJWT(token);
            //3.2 获取用户id
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //3.3 根据用户id从redis中获取用户信息
        String key = "login:" + userId;
        LoginUser loginUser = redisCache.getCacheObject(key);
        //4.如果用户不存在，抛异常，提示错误信息
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }
        //5.如果用户存在，将其存入SecurityContextHolder
        //获取权限信息封装到Authentication中 这里还未做
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //6.所有任务处理完后，放行
        filterChain.doFilter(request,response);
    }
}
