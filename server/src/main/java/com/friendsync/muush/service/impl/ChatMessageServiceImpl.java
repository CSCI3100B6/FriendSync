package com.friendsync.muush.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import com.friendsync.muush.mapper.ChatMessageMapper;
import com.friendsync.muush.repo.ChatMessage;
import com.friendsync.muush.service.ChatMessageService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Override
    public List<ChatMessage> getRecentChatMessagesByConversationID(Long conversationId, int offset, int num) {
        QueryWrapper<ChatMessage> wrapper = new QueryWrapper<ChatMessage>()
            .eq("conversation_id", conversationId)
            .orderByDesc("send_time")
            .last("OFFSET " + offset)
            .last("LIMIT " + num);
        List<ChatMessage> allMsg = chatMessageMapper.selectList(wrapper);
        return allMsg;
    }

    @Override
    public int addMessage(ChatMessage msg) {
        return chatMessageMapper.insert(msg);
    }

    @Override
    public int deleteMessage(ChatMessage msg) {
        return chatMessageMapper.deleteById(msg);
    }
}