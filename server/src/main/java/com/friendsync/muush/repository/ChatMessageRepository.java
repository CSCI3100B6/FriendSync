package com.friendsync.muush.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.friendsync.muush.entity.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByConversationID(Long conversationID, Sort sort);
}
