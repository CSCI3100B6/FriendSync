package com.friendsync.server.muush.entity;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long msgID;

    @Column(nullable=false, unique=false)
    private Long conversationID;

    @Column(nullable=false, unique=false)
    private Long senderID;

    @Column(nullable=false, unique=false)
    private Time sendTime;

    @Column(nullable=true, unique=false)
    private String msgContent;

    public ChatMessage() {}

    public ChatMessage(Long msgID, Long conversationID, Long senderID, Time sendTime, String msgContent) {
        this.msgID = msgID;
        this.conversationID = conversationID;
        this.senderID = senderID;
        this.sendTime = sendTime;
        this.msgContent = msgContent;
    }

    public Long getMsgID() {
        return msgID;
    }

    public void setMsgID(Long msgID) {
        this.msgID = msgID;
    }

    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public Long getConversationID() {
        return conversationID;
    }

    public void setConversationID(Long conversationID) {
        this.conversationID = conversationID;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Time getSendTime() {
        return sendTime;
    }

    public void setSendTime(Time sendTime) {
        this.sendTime = sendTime;
    }
}
