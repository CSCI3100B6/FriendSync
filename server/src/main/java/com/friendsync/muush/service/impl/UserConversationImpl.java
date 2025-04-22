package com.friendsync.muush.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.friendsync.muush.mapper.ConversationMapper;
import com.friendsync.muush.mapper.UserConversationMapper;
import com.friendsync.muush.repo.Conversation;
import com.friendsync.muush.repo.UserConversation;
import com.friendsync.muush.service.UserConversationService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserConversationImpl extends ServiceImpl<UserConversationMapper, UserConversation> implements UserConversationService {
    @Resource
    UserConversationMapper mapper;
    @Resource
    ConversationMapper conversationMapper;

    @Override
    public List<Conversation> getJoinedConversation(Long userId) {
        Wrapper<UserConversation> wrapper = new QueryWrapper<UserConversation>()
            .eq("user_id", userId);
        List<UserConversation> list = mapper.selectList(wrapper);
        List<Long> idList = new ArrayList<>(list.size());
        for (int i = 0; i < idList.size(); i++) {
            idList.set(i, list.get(i).getId());
        }
        return conversationMapper.selectByIds(idList);
    }

    @Override
    public Conversation join(Long userId, Long ConversationId) {
        UserConversation newOne = new UserConversation();
        newOne.setUserId(userId);
        newOne.setConversationId(ConversationId);
        mapper.insert(newOne);
        return conversationMapper.selectById(ConversationId);
    }

    @Override
    public Conversation joinWithLicense(Long userId, Long ConversationId, String license) {
        Conversation c = conversationMapper.selectById(ConversationId);
        if (c.getLicense() != license)
            return null;
        UserConversation newOne = new UserConversation();
        newOne.setUserId(userId);
        newOne.setConversationId(ConversationId);
        mapper.insert(newOne);
        return c;
    }

}
