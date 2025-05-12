package com.friendsync.muush.repo.TDO;

import com.friendsync.muush.repo.Conversation;
import lombok.Data;

@Data
public class CreateConversationRequest {
    private Long ownerId;
    private String name;
    private String information;
    private Conversation.ConversationType type;
    private String license;
}
