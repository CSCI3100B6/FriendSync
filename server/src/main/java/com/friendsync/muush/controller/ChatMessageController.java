package com.friendsync.muush.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.friendsync.muush.repo.ChatMessage;
import com.friendsync.muush.repo.UserConversation;
import com.friendsync.muush.repo.TDO.GetMessageRequest;
import com.friendsync.muush.repo.TDO.SendMessageRequest;
import com.friendsync.muush.service.ChatMessageService;
import com.friendsync.muush.service.UserConversationService;
import com.friendsync.stevenpang.constant.UserConstant;
import com.friendsync.stevenpang.model.User;
import com.friendsync.stevenpang.service.UserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.friendsync.stevenpang.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/message")
public class ChatMessageController {

    @Resource
    private ChatMessageService msgService;
    @Resource
    private UserConversationService userConversationService;
    @Resource
    private UserService userService;

    private boolean permission(Long userId, Long conversationId) {
        User user = userService.getById(userId);
        if (user == null)
            return false;
        if (user.getUserRole() == UserConstant.ADMIN_ROLE)
            return true;
        QueryWrapper<UserConversation> wrapper = new QueryWrapper<UserConversation>()
            .eq("conversation_id", conversationId)
            .eq("user_Id", userId);
        return userConversationService.exists(wrapper);
    }

    @PostMapping("/get")
    public ResponseEntity<?> getMsg(
        HttpServletRequest request,
        @RequestBody GetMessageRequest getMessageRequest) {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(USER_LOGIN_STATE) != null) {
            User user = (User)session.getAttribute(USER_LOGIN_STATE);
            if (!permission(user.getId(), getMessageRequest.getConversationId()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("permission denied");

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
            User user = (User)session.getAttribute(USER_LOGIN_STATE);
            if (!permission(user.getId(), sendMessageRequest.getConversationId()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("permission denied");

            ChatMessage msg = new ChatMessage();
            msg.setConversationId(sendMessageRequest.getConversationId());
            msg.setSenderId(sendMessageRequest.getUserId());
            msg.setMsgContent(sendMessageRequest.getContent());
            msgService.addMessage(msg);
            return ResponseEntity.ok("success");
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
    }
}
