package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.ChatRequest;
import com.sbaproject.sbamindmap.dto.ChatResponse;
import com.sbaproject.sbamindmap.dto.response.ConversationResponse;
import com.sbaproject.sbamindmap.entity.Conversation;
import com.sbaproject.sbamindmap.entity.Message;
import com.sbaproject.sbamindmap.repository.MessageRepository;
import com.sbaproject.sbamindmap.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageRepository messageRepository;

    /**
     * Tạo conversation mới
     */
    @PostMapping("/start")
    public Conversation startConversation(
            @RequestParam("uid") Long userId,
            @RequestParam Long apiKeyId,
            @RequestParam(required = false) String title) {
        return conversationService.startConversation(userId, apiKeyId, title);
    }

    /**
     * Gửi tin nhắn và nhận response từ Gemini + lưu generated data
     */
    @PostMapping("/{conversationId}/message")
    public ChatResponse sendMessage(
            @PathVariable Long conversationId,
            @RequestBody ChatRequest request) {
        return conversationService.sendMessageAndGenerate(conversationId, request);
    }

    /**
     * Lấy lịch sử conversation (không bao gồm messages)
     */
    @GetMapping("/{conversationId}")
    public Conversation getConversation(@PathVariable Long conversationId) {
        return conversationService.getConversation(conversationId);
    }

    /**
     * Lấy conversation kèm tất cả messages
     */
    @GetMapping("/{conversationId}/full")
    public ConversationResponse getConversationWithMessages(@PathVariable Long conversationId) {
        return conversationService.getConversationWithMessages(conversationId);
    }

    /**
     * Lấy tất cả messages của conversation
     */
    @GetMapping("/{conversationId}/messages")
    public List<Message> getMessages(@PathVariable Long conversationId) {
        return messageRepository.findByConversationConversationIdOrderByCreatedAtAsc(conversationId);
    }

    /**
     * Lấy danh sách conversations của user
     */
    @GetMapping("/user")
    public List<Conversation> getUserConversations(@RequestParam("uid") Long userId) {
        return conversationService.getUserConversations(userId);
    }

    /**
     * Xóa (soft delete) conversation
     */
    @DeleteMapping("/{conversationId}")
    public void deleteConversation(@PathVariable Long conversationId) {
        conversationService.deleteConversation(conversationId);
    }
}
