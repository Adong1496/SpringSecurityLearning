package com.hjd.controller;

import com.hjd.domain.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    //@PreAuthorize("hasAuthority('system:dept:list')")
    @PreAuthorize("@ex.hasAuthority('system:dept:list')")
    public String hello() {
        return "hello";
    }
    @RequestMapping("/testCors")
    public ResponseResult testCors() {
        return new ResponseResult(200,"testCors");
    }
}
