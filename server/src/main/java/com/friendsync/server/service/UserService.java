package com.friendsync.server.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendsync.server.entity.User;
import com.friendsync.server.entity.dto.UserDto;
import com.friendsync.server.exception.SignUpException;
import com.friendsync.server.repository.UserRepository;

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
    public User addUser(UserDto user) {
        
        if (user.getEmail() == null) {
            throw new SignUpException("email can not be empty");
        } else if (user.getPassword() == null) {
            throw new SignUpException("password can not be empty");
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
}
