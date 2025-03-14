package com.friendsync.server.muush.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/message")
public class ChatMessageController {

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
