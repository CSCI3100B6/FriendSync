package com.stevenpang.user_center.service;

import com.stevenpang.user_center.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/*
 * 用户服务测试
 * @author steven
 * */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;
    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("testSteven");
        user.setUserAccount("123");
        user.setUserPassword("123456");
        user.setAvatarUrl("156456");
        user.setGender(0);
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

}