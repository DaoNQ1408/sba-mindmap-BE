package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.ChatRequest;
import com.sbaproject.sbamindmap.dto.request.GenerateMindmapRequest;
import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.dto.response.ChatMessageResponse;
import com.sbaproject.sbamindmap.dto.response.ConversationResponse;
import com.sbaproject.sbamindmap.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat & Mindmap Generation", description = "APIs for chat and mindmap generation with OpenAI")
public class ChatController {

    @Autowired
    private final ChatService chatService;

    @PostMapping("/generate-mindmap")
    @Operation(summary = "Generate mindmap from math topic",
               description = "Automatically generate a mindmap for Vietnamese high school math topics using OpenAI")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> generateMindmap(
            @RequestBody GenerateMindmapRequest request) {

        log.info("Received generate mindmap request: topic={}, grade={}",
                request.getTopic(), request.getGrade());

        try {
            ChatMessageResponse response = chatService.generateMindmap(request);
            return ResponseEntity.ok(ApiResponse.success(response, "Mindmap generated successfully"));
        } catch (Exception e) {
            log.error("Error generating mindmap", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(null, "Failed to generate mindmap: " + e.getMessage()));
        }
    }

    @PostMapping("/message")
    @Operation(summary = "Send chat message",
               description = "Send a general chat message to OpenAI")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @RequestBody ChatRequest request) {

        log.info("Received chat message from user: {}", request.getUserId());

        try {
            ChatMessageResponse response = chatService.chat(request);
            return ResponseEntity.ok(ApiResponse.success(response, "Message sent successfully"));
        } catch (Exception e) {
            log.error("Error sending message", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(null, "Failed to send message: " + e.getMessage()));
        }
    }

    @GetMapping("/conversation/{conversationId}/history")
    @Operation(summary = "Get conversation history",
               description = "Retrieve all messages in a conversation")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getConversationHistory(
            @PathVariable Long conversationId) {

        log.info("Fetching conversation history for ID: {}", conversationId);

        try {
            List<ChatMessageResponse> history = chatService.getConversationHistory(conversationId);
            return ResponseEntity.ok(ApiResponse.success(history, "Conversation history retrieved successfully"));
        } catch (Exception e) {
            log.error("Error fetching conversation history", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(null, "Failed to fetch conversation history: " + e.getMessage()));
        }
    }

    @GetMapping("/conversations")
    @Operation(summary = "Get user conversations",
               description = "Get all active conversations for a user")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getUserConversations(
            @RequestParam String userId) {

        log.info("Fetching conversations for user: {}", userId);

        try {
            List<ConversationResponse> conversations = chatService.getUserConversations(userId);
            return ResponseEntity.ok(ApiResponse.success(conversations, "Conversations retrieved successfully"));
        } catch (Exception e) {
            log.error("Error fetching user conversations", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(null, "Failed to fetch conversations: " + e.getMessage()));
        }
    }

    @PostMapping("/conversation/create")
    @Operation(summary = "Create new conversation",
               description = "Create a new conversation for a user")
    public ResponseEntity<ApiResponse<ConversationResponse>> createConversation(
            @RequestParam String userId,
            @RequestParam String title) {

        log.info("Creating new conversation for user: {}, title: {}", userId, title);

        try {
            ConversationResponse conversation = chatService.createConversation(userId, title);
            return ResponseEntity.ok(ApiResponse.success(conversation, "Conversation created successfully"));
        } catch (Exception e) {
            log.error("Error creating conversation", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(null, "Failed to create conversation: " + e.getMessage()));
        }
    }
}
