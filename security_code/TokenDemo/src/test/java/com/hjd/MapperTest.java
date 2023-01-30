package com.hjd;

import com.hjd.domain.User;
import com.hjd.mapper.MenuMapper;
import com.hjd.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MenuMapper menuMapper;
    @Test
    void testMapper() {

        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
    @Test
    public void testBCryptPasswordEncoder() {
        String encode = passwordEncoder.encode("111");
        //$2a$10$AQolAAiJno2MylMv.b.BJ..ZCRArhJaZDoNL6JMHuBnyfzUlEuG36
        System.out.println(encode);
        System.out.println(passwordEncoder.matches("111",
                "$2a$10$AQolAAiJno2MylMv.b.BJ..ZCRArhJaZDoNL6JMHuBnyfzUlEuG36"));

    }

    @Test
    void testSelectPermsByUserId() {
        List<String> list = menuMapper.selectPermsByUserId(2L);
        System.out.println(list);
    }
}
