package com.stevenpang.user_center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stevenpang.user_center.model.User;
import com.stevenpang.user_center.mapper.UserMapper;
import com.stevenpang.user_center.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-02-18 10:35:17
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




