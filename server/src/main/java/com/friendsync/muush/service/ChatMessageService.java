package com.friendsync.muush.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import com.friendsync.muush.data.ChatMessage;
import com.friendsync.muush.mapper.ChatMessageMapper;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatMessageService extends ServiceImpl<ChatMessageMapper, ChatMessage> {

    @Resource
    private ChatMessageMapper msgMapper;

    public List<ChatMessage> getRecentChatMessagesByConversationID(Long conversationID, int offset, int num) {
        return msgMapper.selectChatMessagesOrderByTime(conversationID, offset, num);
    }

    public int addMessage(ChatMessage msg) {
        return msgMapper.insert(msg);
    }
}