package com.friendsync.muush.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.friendsync.muush.repo.Conversation;
import com.friendsync.muush.repo.UserConversation;

public interface UserConversationService extends IService<UserConversation> {
    public List<Conversation> getJoinedConversation(Long userId);
    public Conversation joinTeam(Long userId, Long ConversationId, String license);
    public Conversation joinRoom(Long userId, Long ConversationId);
}
