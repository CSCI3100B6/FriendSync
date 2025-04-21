package com.friendsync.muush.service.impl;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.friendsync.muush.mapper.ConversationMapper;
import com.friendsync.muush.repo.Conversation;
import com.friendsync.muush.repo.Conversation.ConversationType;
import com.friendsync.muush.service.ConversationService;
import com.friendsync.stevenpang.constant.UserConstant;
import com.friendsync.stevenpang.model.User;

@Service
@Slf4j
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {
    @Resource
    private ConversationMapper conversationMapper;
    
    @Override
    public List<Conversation> searchConversations(String s) {
        Wrapper<Conversation> wrapper = new QueryWrapper<Conversation>()
            .like("information", s);
        List<Conversation> list = conversationMapper.selectList(wrapper);
        return list;
    }

    @Override
    public Conversation createConversation(User owner, String info, ConversationType type) {
        Conversation newOne = new Conversation();
        newOne.setOwnerId(owner.getId());
        newOne.setInformation(info);
        newOne.setType(type);
        conversationMapper.insert(newOne);
        return newOne;
    }

    @Override
    public String generateLicense(Long conversationId, User owner) {
        Conversation c = conversationMapper.selectById(conversationId);
        if (c.getOwnerId() == owner.getId() || owner.getUserRole() == UserConstant.ADMIN_ROLE) {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            SecureRandom random = new SecureRandom();
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                key.append(characters.charAt(random.nextInt(characters.length())));
            }
            c.setLicense(key.toString());
            conversationMapper.updateById(c);
            return key.toString();
        } else {
            return null;
        }
    }

    @Override
    public List<Conversation> getOwnConversations(User owner) {
        List<Conversation> list = conversationMapper.selectList(
            new QueryWrapper<Conversation>().eq("owner_id", owner)
        );
        return list;
    }

    @Override
    public boolean deleteConversation(Long conversationId, User owner) {
        Conversation c = conversationMapper.selectById(conversationId);
        if (c.getOwnerId() == owner.getId() || owner.getUserRole() == UserConstant.ADMIN_ROLE) {
            conversationMapper.deleteById(c);
            return true;
        }
        return false;
    }

    @Override
    public List<Conversation> getAllConversation() {
        return conversationMapper.selectList(null);
    }
}
