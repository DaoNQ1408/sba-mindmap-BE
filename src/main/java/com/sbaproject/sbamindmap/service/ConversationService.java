package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.ChatRequest;
import com.sbaproject.sbamindmap.dto.ChatResponse;
import com.sbaproject.sbamindmap.dto.response.ConversationResponse;
import com.sbaproject.sbamindmap.entity.Conversation;

import java.util.List;

public interface ConversationService {
    Conversation startConversation(Long userId, Long apiKeyId, String title);
    ChatResponse sendMessageAndGenerate(Long conversationId, ChatRequest request);
    Conversation getConversation(Long conversationId);
    List<Conversation> getUserConversations(Long userId);
    ConversationResponse getConversationWithMessages(Long conversationId);
    void deleteConversation(Long conversationId);
}
