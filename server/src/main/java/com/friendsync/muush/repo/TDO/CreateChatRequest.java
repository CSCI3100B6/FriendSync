package com.friendsync.muush.repo.TDO;

import java.io.Serializable;

import lombok.Data;

@Data
public class CreateChatRequest implements Serializable {
    private Long ownerId;
    private Long othersId;
}
