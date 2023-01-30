package com.hjd.service;

import com.hjd.domain.ResponseResult;
import com.hjd.domain.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
