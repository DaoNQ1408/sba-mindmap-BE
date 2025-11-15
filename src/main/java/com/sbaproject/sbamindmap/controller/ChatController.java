package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.ChatRequest;
import com.sbaproject.sbamindmap.dto.request.GenerateMindmapRequest;
import com.sbaproject.sbamindmap.dto.response.ChatMessageResponse;
import com.sbaproject.sbamindmap.dto.response.ConversationResponse;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat & Mindmap Generation", description = "APIs for chat and mindmap generation with OpenAI")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/generate-mindmap")
    @Operation(summary = "Generate mindmap from math topic",
               description = "Automatically generate a mindmap for Vietnamese high school math topics (grades 10, 11, 12)")
    public ResponseEntity<ResponseBase> generateMindmap(@RequestBody GenerateMindmapRequest request) {
        log.info(" Received generate mindmap request: topic={}, grade={}", request.getTopic(), request.getGrade());

        ChatMessageResponse response = chatService.generateMindmap(request);
        return ResponseEntity.ok(new ResponseBase(200, "Mindmap generated successfully", response));
    }

    @PostMapping("/message")
    @Operation(summary = "Send chat message",
               description = "Send a general chat message to OpenAI (only math topics for grades 10-12)")
    public ResponseEntity<ResponseBase> sendMessage(@RequestBody ChatRequest request) {
        log.info(" Received chat message from user: {}", request.getUserId());

        ChatMessageResponse response = chatService.chat(request);
        return ResponseEntity.ok(new ResponseBase(200, "Message sent successfully", response));
    }

    @GetMapping("/conversation/{conversationId}/history")
    @Operation(summary = "Get conversation history",
               description = "Retrieve all messages in a conversation")
    public ResponseEntity<ResponseBase> getConversationHistory(@PathVariable Long conversationId) {
        log.info(" Fetching conversation history for ID: {}", conversationId);

        List<ChatMessageResponse> history = chatService.getConversationHistory(conversationId);
        return ResponseEntity.ok(new ResponseBase(200, "Conversation history retrieved successfully", history));
    }

    @GetMapping("/conversations")
    @Operation(summary = "Get user conversations",
               description = "Get all active conversations for a user")
    public ResponseEntity<ResponseBase> getUserConversations(@RequestParam String userId) {
        log.info(" Fetching conversations for user: {}", userId);

        List<ConversationResponse> conversations = chatService.getUserConversations(userId);
        return ResponseEntity.ok(new ResponseBase(200, "Conversations retrieved successfully", conversations));
    }

    @PostMapping("/conversation")
    @Operation(summary = "Create new conversation",
               description = "Create a new conversation for a user")
    public ResponseEntity<ResponseBase> createConversation(
            @RequestParam String userId,
            @RequestParam(required = false) String title) {
        log.info("➕ Creating conversation for user: {}", userId);

        ConversationResponse response = chatService.createConversation(userId, title);
        return ResponseEntity.ok(new ResponseBase(200, "Conversation created successfully", response));
    }
}
