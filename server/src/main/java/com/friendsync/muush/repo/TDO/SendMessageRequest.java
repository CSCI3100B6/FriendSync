package com.friendsync.muush.repo.TDO;

import java.io.Serializable;

import lombok.Data;

@Data
public class SendMessageRequest implements Serializable {
    Long conversationId;
    Long userId;
    String content;
}
