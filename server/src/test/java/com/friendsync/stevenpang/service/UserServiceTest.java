//package com.friendsync.stevenpang.service;
//
//import jakarta.annotation.Resource;
//import org.junit.Assert;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//import java.util.List;
//
//
///*
// * 用户服务测试
// * @author steven
// * */
//@SpringBootTest
//public class UserServiceTest {
//
//    @Resource
//    private UserService userService;
//    @Test
//    public void testAddUser() {
//        User user = new User();
//        user.setUsername("testSteven");
//        user.setUserAccount("123");
//        user.setUserPassword("123456");
//        user.setAvatarUrl("156456");
//        user.setGender(0);
//        user.setPhone("123");
//        user.setEmail("456");
//        boolean result = userService.save(user);
//        System.out.println(user.getId());
//        Assertions.assertTrue(result);
//    }
//
//    @Test
//    void userRegister() {
//        String userAccount = "Steven";
//        String userPassword = "";
//        String checkPassword = "123456";
//        String planetCode = "1";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "st";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "Steven";
//        userPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "steven pang";
//        userPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userPassword = "12345678";
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "testSteven";
//        userPassword = "123456789";
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "Steven203426";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertTrue(result>=0);
//
//
//    }
//
//    @Test
//    public void testSearchUserByTags() {
//        List<String> tagNameList = Arrays.asList("java", "python");
//        List<User> userList = userService.searchUserByTags(tagNameList);
//        Assert.assertNotNull(userList);
//    }
//}