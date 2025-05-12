package com.friendsync.stevenpang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.friendsync.stevenpang.model.User;
import com.friendsync.stevenpang.model.request.UserDTO;
import com.friendsync.stevenpang.model.request.UserRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     *
     * @param tagList
     * @return
     */
    List<User> searchUserByTags(List<String> tagList);

    /**
     * 随机获取五条数据
     * @return
     */
    List<User> getRandomRecords();

    /**
     * 查询用户
     * @param userRequest
     * @return
     */
    List<User> searchUsers(UserRequest userRequest);

}
