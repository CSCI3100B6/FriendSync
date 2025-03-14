package com.stevenpang.user_center.service;

import com.stevenpang.user_center.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author lenovo
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-02-18 10:35:17
*/
public interface UserService extends IService<User> {




    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return  新用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @return  返回脱敏的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originUser);
}
