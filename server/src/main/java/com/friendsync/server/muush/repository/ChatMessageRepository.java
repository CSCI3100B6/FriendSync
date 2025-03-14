package com.friendsync.server.muush.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.friendsync.server.muush.entity.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByConversationID(Long conversationID, Sort sort);
}
