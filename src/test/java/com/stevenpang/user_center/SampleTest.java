package com.stevenpang.user_center;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.stevenpang.user_center.mapper.UserMapper;
import com.stevenpang.user_center.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

@SpringBootTest
public class SampleTest {


    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }

}