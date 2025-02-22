package com.friendsync.server.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendsync.server.entity.User;
import com.friendsync.server.entity.dto.UserDto;
import com.friendsync.server.exception.LoginException;
import com.friendsync.server.exception.RegisterException;
import com.friendsync.server.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService implements IUserService {
    
    @Autowired
    private UserRepository userRepository;

    MessageDigest digest;

    public UserService() throws NoSuchAlgorithmException {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
    }
    
    @Override
    public User register(UserDto user) {
        User userInfo = userRepository.findByEmail(user.getEmail());
        if (userInfo != null) {
            throw new RegisterException("exist account, repeat sign up");
        }
        if (user.getEmail() == null) {
            throw new RegisterException("email can not be empty");
        } else if (user.getPassword() == null) {
            throw new RegisterException("password can not be empty");
        }

        byte[] hashBytes = digest.digest(user.getPassword().getBytes());
        User newUser = new User(
            user.getEmail(),
            String.format("%064x", new BigInteger(1, hashBytes)),
            user.getName()
        );
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User login(UserDto user, HttpSession session) {
        User userInfo = userRepository.findByEmail(user.getEmail());
        if (userInfo == null) {
            throw new LoginException("not sign up email");
        } else if (user.getPassword() == null) {
            throw new LoginException("empty password");
        }
        byte[] hashBytes = digest.digest(user.getPassword().getBytes());
        String hashString = String.format("%064x", new BigInteger(1, hashBytes));
        if (hashString == null ? userInfo.getPassword() == null : hashString.equals(userInfo.getPassword())) {
            userInfo.setCurrentSession(session.getId());
            userRepository.save(userInfo);
            return userInfo;
        } else {
            return null;
        }
    }
    
}
