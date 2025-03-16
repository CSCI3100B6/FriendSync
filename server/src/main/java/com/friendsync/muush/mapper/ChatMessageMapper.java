package com.friendsync.muush.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.friendsync.muush.data.ChatMessage;

public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    /**
     * select chat messages by time order
     * @param senderId
     * @return a chat msg list
     */
    List<ChatMessage> selectChatMessagesOrderByTime(
        @Param("senderId") Long senderId,
        @Param("offset") Integer offset,
        @Param("pageSize") Integer pageSize);
}
