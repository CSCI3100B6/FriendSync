package com.friendsync.muush.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.friendsync.muush.repo.ChatMessage;

public interface ChatMessageService extends IService<ChatMessage> {
    public List<ChatMessage> getRecentChatMessagesByConversationID(Long conversationId, int offset, int num);
    public int addMessage(ChatMessage msg);
    public int deleteMessage(ChatMessage msgId);
}
