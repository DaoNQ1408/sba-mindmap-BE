package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.ChatRequest;
import com.sbaproject.sbamindmap.dto.ChatResponse;
import com.sbaproject.sbamindmap.dto.response.ConversationResponse;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.entity.Conversation;
import com.sbaproject.sbamindmap.entity.Message;
import com.sbaproject.sbamindmap.service.ConversationService;
import com.sbaproject.sbamindmap.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Conversation Management", description = "APIs for managing conversations and messages")
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    /**
     * Tạo conversation mới
     */
    @PostMapping("/start")
    @Operation(summary = "Start new conversation", description = "Create a new conversation for a user")
    public ResponseEntity<ResponseBase> startConversation(
            @RequestParam("uid") Long userId,
            @RequestParam Long apiKeyId,
            @RequestParam(required = false) String title) {
        log.info("Starting new conversation for user: {}", userId);

        Conversation conversation = conversationService.startConversation(userId, apiKeyId, title);
        return ResponseEntity.ok(new ResponseBase(200, "Conversation created successfully", conversation));
    }

    /**
     * Gửi tin nhắn và nhận response từ Gemini + lưu generated data
     */
    @PostMapping("/{conversationId}/message")
    @Operation(summary = "Send message", description = "Send a message in a conversation and get AI response")
    public ResponseEntity<ResponseBase> sendMessage(
            @PathVariable Long conversationId,
            @RequestBody ChatRequest request) {
        log.info("Sending message to conversation: {}", conversationId);

        ChatResponse response = conversationService.sendMessageAndGenerate(conversationId, request);
        return ResponseEntity.ok(new ResponseBase(200, "Message sent successfully", response));
    }

    /**
     * Lấy lịch sử conversation (không bao gồm messages)
     */
    @GetMapping("/{conversationId}")
    @Operation(summary = "Get conversation", description = "Retrieve conversation details without messages")
    public ResponseEntity<ResponseBase> getConversation(@PathVariable Long conversationId) {
        log.info("Fetching conversation: {}", conversationId);

        Conversation conversation = conversationService.getConversation(conversationId);
        return ResponseEntity.ok(new ResponseBase(200, "Conversation retrieved successfully", conversation));
    }

    /**
     * Lấy conversation kèm tất cả messages
     */
    @GetMapping("/{conversationId}/full")
    @Operation(summary = "Get conversation with messages", description = "Retrieve conversation with all messages")
    public ResponseEntity<ResponseBase> getConversationWithMessages(@PathVariable Long conversationId) {
        log.info("Fetching conversation with messages: {}", conversationId);

        ConversationResponse response = conversationService.getConversationWithMessages(conversationId);
        return ResponseEntity.ok(new ResponseBase(200, "Conversation with messages retrieved successfully", response));
    }

    /**
     * Lấy tất cả messages của conversation
     */
    @GetMapping("/{conversationId}/messages")
    @Operation(summary = "Get messages", description = "Retrieve all messages in a conversation")
    public ResponseEntity<ResponseBase> getMessages(@PathVariable Long conversationId) {
        log.info(" Fetching messages for conversation: {}", conversationId);

        List<Message> messages = messageService.getMessagesByConversation(conversationId);
        return ResponseEntity.ok(new ResponseBase(200, "Messages retrieved successfully", messages));
    }

    /**
     * Lấy danh sách conversations của user
     */
    @GetMapping("/user")
    @Operation(summary = "Get user conversations", description = "Retrieve all conversations for a user")
    public ResponseEntity<ResponseBase> getUserConversations(@RequestParam("uid") Long userId) {
        log.info("Fetching conversations for user: {}", userId);

        List<Conversation> conversations = conversationService.getUserConversations(userId);
        return ResponseEntity.ok(new ResponseBase(200, "User conversations retrieved successfully", conversations));
    }

    /**
     * Xóa (soft delete) conversation
     */
    @DeleteMapping("/{conversationId}")
    @Operation(summary = "Delete conversation", description = "Delete a conversation (soft delete)")
    public ResponseEntity<ResponseBase> deleteConversation(@PathVariable Long conversationId) {
        log.info("🗑Deleting conversation: {}", conversationId);

        conversationService.deleteConversation(conversationId);
        return ResponseEntity.ok(new ResponseBase(200, "Conversation deleted successfully", null));
    }
}
