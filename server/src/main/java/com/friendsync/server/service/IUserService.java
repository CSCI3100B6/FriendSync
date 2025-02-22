package com.friendsync.server.service;

import com.friendsync.server.entity.User;
import com.friendsync.server.entity.dto.UserDto;

import jakarta.servlet.http.HttpSession;

public interface IUserService {
    /**
     * create new user
     * @param user new user
     * @return the new User in the database
     */
    User register(UserDto user);

    /**
     * user login
     * will check the password and change the currentSession
     * @param user user
     * @param session http session
     * @return the User in the database if seccess. Otherwise null ptr.
     */
    User login(UserDto user, HttpSession session);
}
