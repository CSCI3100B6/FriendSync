package com.friendsync.muush.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.floatThat;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.friendsync.muush.repo.ChatMessage;
import jakarta.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatMessageServiceTest {

    @Resource
    private ChatMessageService chatMessageService;
    
    @Test
    public void testAddMsg() {
        assertNotNull(chatMessageService);
        
        for (int i = 0; i < 10; i++)
        {
            ChatMessage msg = new ChatMessage();
            msg.setConversationId(0L);
            msg.setSenderId((long)i * 10L);
            msg.setSendTime(new Timestamp(System.currentTimeMillis() + i * 1000L));
            msg.setMsgContent("test msg " + i);

            int rtn = chatMessageService.addMessage(msg);
            assertEquals(1, rtn);
        }
    }

    @Test
    public void testGetMsg() {
        List<ChatMessage> msgList =
            chatMessageService.getRecentChatMessagesByConversationID(
                0L, 0, 100);
        System.out.println(msgList.size());
        for (ChatMessage chatMessage : msgList) {
            System.out.println(chatMessage.getSendTime());
        }
    }

    @Test
    public void testDeleteMsg() {
        List<ChatMessage> msgList =
            chatMessageService.getRecentChatMessagesByConversationID(
                0L, 0, 5);
        for (ChatMessage chatMessage : msgList) {
            assertEquals(1, chatMessageService.deleteMessage(chatMessage));
            System.out.println("delete msg: " + chatMessage.toString());
        }
    }
}