package com.friendsync.muush.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.friendsync.muush.repo.Conversation;

public interface ConversationService extends IService<Conversation> {
    public List<Conversation> searchConversations(String s);
    public Conversation createConversation(Long owner, String info, Conversation.ConversationType type);
    public String generateLicense(Long conversationId, Long ownerId);
    public List<Conversation> getOwnConversations(Long ownerId);
    public boolean deleteConversation(Long conversationId, Long ownerId);
}
