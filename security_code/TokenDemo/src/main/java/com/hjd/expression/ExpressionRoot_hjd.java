package com.hjd.expression;


import com.hjd.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.plaf.PanelUI;
import java.util.List;

@Component("ex")
public class ExpressionRoot_hjd {

    public boolean hasAuthority(String authority) {

        //获取当前用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        //获取当前用户权限集合是否包含authority
        return permissions.contains(authority);
    }
}
