package com.friendsync.muush.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.friendsync.muush.repo.Conversation;
import com.friendsync.muush.repo.TDO.CreateConversationRequest;
import com.friendsync.muush.service.ConversationService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.friendsync.stevenpang.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    @Resource
    private ConversationService service;

    @GetMapping("/search")
    public ResponseEntity<?> searchConversation(
        HttpServletRequest request,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
            HttpSession session = request.getSession();
            if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");

            if (!keyword.equals("")) {
                return ResponseEntity.status(HttpStatus.OK).body(service.searchConversations(keyword));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("need GET param keyword"); 
            }
        }
    
    @PostMapping("/create")
    public ResponseEntity<?> createConversation(
        HttpServletRequest request,
        @RequestBody CreateConversationRequest createConversationRequest
    ) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
        Conversation c = service.createConversation(
            createConversationRequest.getOwnerId(),
            createConversationRequest.getInformation(),
            createConversationRequest.getType());
        if (c != null)
            return ResponseEntity.ok(c);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }

    @GetMapping("/generate")
    public ResponseEntity<?> genLicense(
        HttpServletRequest request,
        @RequestParam(value = "id") Long id,
        @RequestParam(value = "owner") Long owner
    ) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
        String s = service.generateLicense(id, owner);
        if (s != null)
            return ResponseEntity.ok(s);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }

    @GetMapping("/getown")
    public ResponseEntity<?> getOwn(
        HttpServletRequest request,
        @RequestParam("owner") Long owner
    ) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
        
        
        return ResponseEntity.ok(service.getOwnConversations(owner));
    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(   
        HttpServletRequest request,
        @RequestParam("id") Long id,
        @RequestParam("owner") Long owner
     ) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("login first");
        boolean result = service.deleteConversation(id, owner);
        if (result)
            return ResponseEntity.ok("success");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }
}
