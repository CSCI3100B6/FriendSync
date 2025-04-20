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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    @Resource
    private ConversationService service;

    @GetMapping("/search")
    public ResponseEntity<?> searchConversation(
        HttpServletRequest request,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
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
        HttpServlet request,
        @RequestParam(value = "id") Long id,
        @RequestParam(value = "owner") Long owner
    ) {
        String s = service.generateLicense(id, owner);
        if (s != null)
            return ResponseEntity.ok(s);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }

    @GetMapping("/getown")
    public ResponseEntity<?> getOwn(@RequestParam("owner") Long owner) {
        return ResponseEntity.ok(service.getOwnConversations(owner));
    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id, @RequestParam("owner") Long owner) {
        boolean result = service.deleteConversation(id, owner);
        if (result)
            return ResponseEntity.ok("success");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
    }
}
