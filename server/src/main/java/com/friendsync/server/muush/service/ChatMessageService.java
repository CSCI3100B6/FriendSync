package com.friendsync.server.muush.service;

import static java.lang.Math.min;
import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.friendsync.server.muush.entity.ChatMessage;
import com.friendsync.server.muush.repository.ChatMessageRepository;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository msgRepo;

    public List<ChatMessage> getRecentChatMessagesByConversationID(Long conversationID, int num) {
        Sort sort = Sort.by(Sort.Direction.ASC, "sendTime");
        List<ChatMessage> allMsg = msgRepo.findAllByConversationID(conversationID, sort);
        return allMsg.subList(0, min(num, allMsg.size()));
    }

    public List<ChatMessage> getRecentChatMessagesByConversationID(Long conversationID, Time beforeTime, int num) {
        Sort sort = Sort.by(Sort.Direction.ASC, "sendTime");
        List<ChatMessage> allMsg = msgRepo.findAllByConversationID(conversationID, sort);
        // Binary Search
        int l = 0, r = allMsg.size() - 1;
        int firstIdx = r;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (allMsg.get(mid).getSendTime().before(beforeTime)) {
                r = mid - 1;
                firstIdx = mid;
            } else {
                l = mid + 1;
            }
        }
        return allMsg.subList(
            min(firstIdx, allMsg.size()),
            min(firstIdx + num, allMsg.size()));
    }
}