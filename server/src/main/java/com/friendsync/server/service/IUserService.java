package com.friendsync.server.service;

import com.friendsync.server.entity.User;
import com.friendsync.server.entity.dto.UserDto;

public interface IUserService {
    /**
     * create new user
     * @param user new user
     */
    User addUser(UserDto user);
}
