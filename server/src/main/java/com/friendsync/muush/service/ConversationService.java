package com.friendsync.muush.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.friendsync.muush.repo.Conversation;
import com.friendsync.stevenpang.model.domain.User;

public interface ConversationService extends IService<Conversation> {
    public List<Conversation> searchConversations(String s);
    public Conversation createConversation(User owner, String name, String info, Conversation.ConversationType type);
    public String generateLicense(Long conversationId, User owner);
    public List<Conversation> getOwnConversations(User owner);
    public boolean deleteConversation(Long conversationId, User owner);

    public List<Conversation> getAllConversation();
}
