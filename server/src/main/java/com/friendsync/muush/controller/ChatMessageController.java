package com.friendsync.muush.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friendsync.muush.repo.ChatMessage;
import com.friendsync.muush.repo.TDO.GetMessageRequest;
import com.friendsync.muush.repo.TDO.SendMessageRequest;
import com.friendsync.muush.service.impl.ChatMessageServiceImpl;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.friendsync.stevenpang.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/message")
public class ChatMessageController {

    @Resource
    private ChatMessageServiceImpl msgService;

    @PostMapping("/get")
    public ResponseEntity<?> getMsg(
        HttpServletRequest request,
        @RequestBody GetMessageRequest getMessageRequest) {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(USER_LOGIN_STATE) != null) {
            List<ChatMessage> msgList = msgService.getRecentChatMessagesByConversationID(
                    getMessageRequest.getConversationId(),
                    getMessageRequest.getOffset(),
                    getMessageRequest.getNum());
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(msgList);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
    }
    
    @PostMapping("/send")
    public ResponseEntity<?> sendMsg(
        HttpServletRequest request,
        @RequestBody SendMessageRequest sendMessageRequest) {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(USER_LOGIN_STATE) != null) {
            ChatMessage msg = new ChatMessage();
            msg.setConversationId(sendMessageRequest.getConversationId());
            msg.setSenderId(sendMessageRequest.getUserId());
            msg.setMsgContent(sendMessageRequest.getContent());
            msgService.addMessage(msg);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
    }
}
