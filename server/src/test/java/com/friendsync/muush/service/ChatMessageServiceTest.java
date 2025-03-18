package com.friendsync.muush.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Time;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.friendsync.muush.repo.ChatMessage;
import jakarta.annotation.Resource;

@SpringBootTest
public class ChatMessageServiceTest {

    @Resource
    private ChatMessageService chatMessageService;
    
    @Test
    public void testAddMsg() {
        assertNotNull(chatMessageService);
        
        ChatMessage msg = new ChatMessage();
        msg.setConversationId(123L);
        msg.setMsgContent("test msg");
        msg.setSenderId(321L);
        msg.setSendTime(new Time(System.currentTimeMillis()));

        int rtn = chatMessageService.addMessage(msg);
        assertEquals(1, rtn);

        List<ChatMessage> msgList = chatMessageService.getRecentChatMessagesByConversationID(
                msg.getConversationId(),
                0, 5);
        System.out.println(msgList);

        rtn = chatMessageService.deleteMessage(msg.getId());
        assertEquals(1, rtn);
    }
}