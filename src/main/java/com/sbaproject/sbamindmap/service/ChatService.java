package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.ChatRequest;
import com.sbaproject.sbamindmap.dto.request.GenerateMindmapRequest;
import com.sbaproject.sbamindmap.dto.response.ChatMessageResponse;
import com.sbaproject.sbamindmap.dto.response.ConversationResponse;

import java.util.List;

public interface ChatService {

    /**
     * Tạo mindmap tự động từ chủ đề Toán học
     */
    ChatMessageResponse generateMindmap(GenerateMindmapRequest request);

    /**
     * Chat thông thường với AI
     */
    ChatMessageResponse chat(ChatRequest request);

    /**
     * Lấy lịch sử hội thoại
     */
    List<ChatMessageResponse> getConversationHistory(Long conversationId);

    /**
     * Lấy danh sách conversations của user
     */
    List<ConversationResponse> getUserConversations(String userId);

    /**
     * Tạo conversation mới
     */
    ConversationResponse createConversation(String userId, String title);
}

