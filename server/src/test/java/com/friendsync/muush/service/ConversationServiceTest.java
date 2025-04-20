package com.friendsync.muush.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.friendsync.muush.repo.Conversation;

import jakarta.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConversationServiceTest {
    @Resource
    private ConversationService conversationService;

    @Test
    public void testSearch() {
        List<Conversation> l = conversationService.searchConversations("1");
        System.out.println(l.size());
        for (Conversation conversation : l) {
            System.out.println(conversation.getInformation());
        }
    }

    @Test
    public void addConversation() {
        conversationService.createConversation(0L, "test 0", Conversation.ConversationType.CHAT);
        conversationService.createConversation(1L, "test 1", Conversation.ConversationType.TEAM);
        conversationService.createConversation(2L, "test 2", Conversation.ConversationType.ROOM);
    }

    @Test
    public void testGenerateLicense() {
        String lic = conversationService.generateLicense(1L, 0L);
        System.out.println(lic);
    }

    @Test
    public void testGetOwn() {
        System.out.println(conversationService.getOwnConversations(1L).size());
    }
}
