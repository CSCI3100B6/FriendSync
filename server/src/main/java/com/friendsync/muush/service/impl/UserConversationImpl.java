package com.friendsync.muush.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<UserConversation> arr = mapper.selectList(wrapper);
        ArrayList<Long> ids = new ArrayList<Long>();
        for (UserConversation uc : arr) {
            ids.add(uc.getConversationId());
        }
        if (ids.isEmpty())
            return Arrays.asList();
        return conversationMapper.selectByIds(ids);
    }

    @Override
    public Conversation join(Long userId, Long conversationId) {
        QueryWrapper<UserConversation> wrapper = new QueryWrapper<UserConversation>()
            .eq("conversation_id", conversationId)
            .eq("user_Id", userId);
        if (this.exists(wrapper))
            return conversationMapper.selectById(conversationId);
        
        UserConversation newOne = new UserConversation();
        newOne.setUserId(userId);
        newOne.setConversationId(conversationId);
        mapper.insert(newOne);
        return conversationMapper.selectById(conversationId);
    }

    @Override
    public Conversation joinWithLicense(Long userId, Long conversationId, String license) {
        Conversation c = conversationMapper.selectById(conversationId);
        QueryWrapper<UserConversation> wrapper = new QueryWrapper<UserConversation>()
            .eq("conversation_id", conversationId)
            .eq("user_Id", userId);
        if (this.exists(wrapper))
            return c;

        if (!c.getLicense().equals(license))
            return null;
        UserConversation newOne = new UserConversation();
        newOne.setUserId(userId);
        newOne.setConversationId(conversationId);
        mapper.insert(newOne);
        return c;
    }

    @Override
    public Boolean leave(Long userId, Long conversationId, Long newOwner) {
        Conversation c = conversationMapper.selectById(conversationId);
        boolean isOwner = c.getOwnerId() == userId;

        // change owner
        long cnt = mapper.selectCount(new QueryWrapper<UserConversation>().eq("conversation_id", conversationId));
        if (isOwner && cnt > 1) {
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("user_id", newOwner);
            columnMap.put("conversation_id", conversationId);
            boolean validNewOwner = mapper.selectByMap(columnMap) != null;
            if (!validNewOwner || newOwner == userId)
                return false;
            c.setOwnerId(newOwner);
            conversationMapper.updateById(c);
        }

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id", userId);
        columnMap.put("conversation_id", conversationId);
        mapper.deleteByMap(columnMap);
        
        // delete the conversation if is the last person
        cnt = mapper.selectCount(new QueryWrapper<UserConversation>().eq("conversation_id", conversationId));
        if (cnt == 0) {
            conversationMapper.deleteById(conversationId);
        }

        return true;
    }

}
