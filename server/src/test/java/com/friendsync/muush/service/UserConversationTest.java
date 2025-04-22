package com.friendsync.muush.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.friendsync.muush.repo.Conversation;
import com.friendsync.muush.repo.Conversation.ConversationType;
import com.friendsync.stevenpang.model.User;

import jakarta.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserConversationTest {
    @Resource
    UserConversationService service;
    @Resource
    ConversationService conversationService;

    @Test
    public void testGetJoined() {
        User user = new User();
        user.setId(0L);
        List<Conversation> list = service.getJoinedConversation(0L);
        for (Conversation c : list)
            System.out.println(c.getName());
    }

    @Test
    public void testJoin() {
        User user = new User();
        user.setId(0L);
        Conversation c;
        c = conversationService.createConversation(user, "test1", "", ConversationType.CHAT);
        service.join(user.getId(), c.getId());
        c = conversationService.createConversation(user, "test2", "", ConversationType.ROOM);
        service.join(user.getId(), c.getId());
        c = conversationService.createConversation(user, "test3", "", ConversationType.TEAM);
        service.join(user.getId(), c.getId());
    }

    @Test
    public void testLicense() {
        User user = new User();
        user.setId(0L);
        Conversation c;
        c = conversationService.createConversation(user, "test for license", "", ConversationType.TEAM);
        String license = conversationService.generateLicense(c.getId(), user);
        System.out.println(license);
        User user2 = new User();
        user2.setId(2L);
        service.joinWithLicense(user2.getId(), c.getId(), license);
        List<Conversation> list = service.getJoinedConversation(user2.getId());
        for (Conversation cc : list)
            System.out.println(cc.getName());
    }
}
