package com.friendsync.muush.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.friendsync.muush.repo.Conversation;
import com.friendsync.muush.repo.UserConversation;

public interface UserConversationService extends IService<UserConversation> {
    public List<Conversation> getJoinedConversation(Long userId);
    public Conversation join(Long userId, Long ConversationId);
    public Conversation joinWithLicense(Long userId, Long ConversationId, String license);

    public Boolean leave(Long userId, Long conversationId, Long newOwner);
}
