package com.friendsync.muush.controller;

import com.friendsync.stevenpang.model.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.friendsync.muush.service.UserConversationService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.friendsync.stevenpang.constant.UserConstant.USER_LOGIN_STATE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/user-conversation")
public class UserConversationController {
    @Resource
    private UserConversationService service;

    @GetMapping("/joined")
    public ResponseEntity<?> joined(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        return ResponseEntity.ok(service.getJoinedConversation(user.getId()));
    }
    
    @GetMapping("/join-room")
    public ResponseEntity<?> joinRoom(
        HttpServletRequest request,
        @RequestParam(value = "id", required = true, defaultValue = "") Long id) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        return ResponseEntity.ok(service.join(user.getId(), id));
    }

    @GetMapping("/join-team")
    public ResponseEntity<?> joinTeam(
        HttpServletRequest request,
        @RequestParam(value = "id", required = true, defaultValue = "") Long id,
        @RequestParam(value = "license", required = true, defaultValue = "") String license) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        var result = service.joinWithLicense(user.getId(), id, license);
        if (result == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong license");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/leave")
    public ResponseEntity<?> leave(
        HttpServletRequest request,
        @RequestParam(value = "id", required = true, defaultValue = "") Long id,
        @RequestParam(value = "new_owner", required = false, defaultValue = "") Long newOwner) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        boolean success = service.leave(user.getId(), id, newOwner);
        if (success)
            return ResponseEntity.ok("success");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user or new owner not in the conversation");
    }
}
