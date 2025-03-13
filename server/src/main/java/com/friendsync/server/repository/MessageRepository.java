package com.friendsync.server.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.friendsync.server.entity.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findAllByConversationID(Long conversationID);
}
