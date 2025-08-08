package com.org.bloomfilterstudy.service.impl;

import com.org.bloomfilterstudy.domain.User;
import com.org.bloomfilterstudy.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    private UserService userService;

    @Test
    void getUserById() {
        User userById = userService.getUserById(1L);
        System.out.println(userById);
    }

    @Test
    void addUser() {
        // 链式构造user对象
        User user = User.builder().id(1L).name("张三").build();
        userService.addUser(user);
    }

    @Test
    void recommend() {
        List<Long> contentIds = Arrays.asList(1L, 2L, 3L, 4L, 5L); // 模拟内容池
        List<Long> recommend = userService.recommend(2L, contentIds);
        System.out.println(recommend);
    }
}