package com.friendsync.muush.data;

import java.io.Serializable;
import java.sql.Time;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@TableName(value = "chat_msg")
@Data
public class ChatMessage implements Serializable {
    @TableId
    private Long msgId;

    private Long conversationId;

    private Long senderId;

    private Time sendTime;

    private String msgContent;
}
