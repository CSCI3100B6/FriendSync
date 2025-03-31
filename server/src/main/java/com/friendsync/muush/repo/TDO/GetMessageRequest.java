package com.friendsync.muush.repo.TDO;

import java.io.Serializable;

import lombok.Data;

@Data
public class GetMessageRequest implements Serializable {
    Long conversationId;
    Integer offset;
    Integer num;
}
