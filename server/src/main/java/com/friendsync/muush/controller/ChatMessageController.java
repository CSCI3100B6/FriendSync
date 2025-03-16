package com.friendsync.muush.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friendsync.muush.service.ChatMessageService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/message")
public class ChatMessageController {

    @Resource
    private ChatMessageService msgService;

    @PostMapping("/get")
    public String getMsg(HttpServletRequest request /* msgDTO Session */ ) {
        // TODO:
        return new String();
    }
    
    @PostMapping("/send")
    public String sendMsg(HttpServletRequest request /* msgDTO Session */ ) {
        // TODO:
        return new String();
    }
}
