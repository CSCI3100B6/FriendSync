package com.friendsync.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friendsync.server.entity.User;
import com.friendsync.server.entity.dto.UserDto;
import com.friendsync.server.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> regester(
        @RequestBody UserDto user) {
        try {
            userService.register(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("seccess to register a new user", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
        HttpServletRequest request,
        @RequestBody UserDto user) {
        User userInfo;
        try {
            userInfo = userService.login(user, request.getSession());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (userInfo != null)
            return new ResponseEntity<>("seccess to login", HttpStatus.OK);
        else
            return new ResponseEntity<>("wrong password", HttpStatus.UNAUTHORIZED);
    }
    
}
