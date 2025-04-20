package com.friendsync.muush.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.friendsync.muush.repo.Conversation;

@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> { }
