package com.friendsync.muush.service;

import java.util.List;

import com.friendsync.stevenpang.model.domain.User;
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
        List<Conversation> l = conversationService.searchConversations("est");
        System.out.println(l.size());
        for (Conversation conversation : l) {
            System.out.println(conversation.getInformation());
        }
    }

    @Test
    public void addConversation() {
        User owner = new User();
        owner.setId(1L);
        conversationService.createConversation(owner, "test 0", "info abc", Conversation.ConversationType.CHAT);
        conversationService.createConversation(owner, "test 1", "info ccb", Conversation.ConversationType.TEAM);
        owner.setId(2L);
        conversationService.createConversation(owner, "test 2", "info bin", Conversation.ConversationType.ROOM);
    }

    @Test
    public void testGenerateLicense() {
        User owner = new User();
        owner.setId(1L);
        String lic = conversationService.generateLicense(5L, owner);
        System.out.println(lic);
    }

    @Test
    public void testGetOwn() {
        User owner = new User();
        owner.setId(1L);
        System.out.println(conversationService.getOwnConversations(owner).size());
    }

    @Test
    public void testDelete() {
        User owner = new User();
        owner.setId(1L);
        assert(conversationService.deleteConversation(4L, owner));
    }

    @Test
    public void testGetAll() {
        System.out.println(conversationService.getAllConversation().size());
    }
}
