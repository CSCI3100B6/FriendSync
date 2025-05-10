package com.friendsync.muush.repo.TDO;

import java.io.Serializable;

import com.friendsync.muush.repo.Conversation;

import lombok.Data;

@Data
public class CreateTeamRoomRequest implements Serializable {
    private Long ownerId;
    private String name;
    private String information;
    private Conversation.ConversationType type;
}
