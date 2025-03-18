package com.friendsync.muush.repo;

import java.io.Serializable;
import java.sql.Time;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@TableName(value = "chat_messages")
@Data
public class ChatMessage implements Serializable {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("conversation_id")
    private Long conversationId;

    @TableField("sender_id")
    private Long senderId;

    @TableField("send_time")
    private Time sendTime;

    @TableField("msg_content")
    private String msgContent;
}
