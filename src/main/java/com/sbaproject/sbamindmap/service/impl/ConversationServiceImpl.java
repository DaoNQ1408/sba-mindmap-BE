package com.sbaproject.sbamindmap.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbaproject.sbamindmap.constant.MindmapPromptTemplate;
import com.sbaproject.sbamindmap.dto.ChatRequest;
import com.sbaproject.sbamindmap.dto.ChatResponse;
import com.sbaproject.sbamindmap.dto.response.ConversationResponse;
import com.sbaproject.sbamindmap.entity.*;
import com.sbaproject.sbamindmap.enums.MindmapTemplateType;
import com.sbaproject.sbamindmap.mapper.ConversationMapper;
import com.sbaproject.sbamindmap.repository.*;
import com.sbaproject.sbamindmap.service.ConversationService;
import com.sbaproject.sbamindmap.service.EntitlementService;
import com.sbaproject.sbamindmap.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final GeneratedDataRepository generatedDataRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private final GeminiService geminiService;
    private final EntitlementService entitlementService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConversationMapper conversationMapper;

    @Override
    @Transactional
    public Conversation startConversation(Long userId, Long apiKeyId, String title) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new RuntimeException("ApiKey not found: " + apiKeyId));

        Conversation conv = Conversation.builder()
                .user(user)
                .apiKey(apiKey)
                .title(title == null ? "New Chat" : title)
                .isActive(true)
                .build();

        return conversationRepository.save(conv);
    }

    @Override
    @Transactional
    public ChatResponse sendMessageAndGenerate(Long conversationId, ChatRequest request) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));

        ApiKey apiKey = conversation.getApiKey();
        if (apiKey == null) throw new RuntimeException("Conversation has no api key assigned");

        // Check entitlement
        if (!entitlementService.isKeyUsable(conversation.getUser().getId(), apiKey.getApiKeyId())) {
            throw new RuntimeException("API key not usable or exhausted");
        }

        // Save user message
        Message userMessage = Message.builder()
                .conversation(conversation)
                .role("user")
                .content(request.getPrompt())
                .build();
        messageRepository.save(userMessage);

        // Determine system instruction
        String systemInstruction = null;
        if (request.getSystemInstruction() != null && !request.getSystemInstruction().isBlank()) {
            systemInstruction = request.getSystemInstruction();
        } else if (request.getTemplateType() != null) {
            systemInstruction = request.getTemplateType().getInstruction();
        } else if (conversation.getTitle() != null && conversation.getTitle().toLowerCase().contains("mindmap")) {
            systemInstruction = MindmapPromptTemplate.MINDMAP_JSON_INSTRUCTION;
        } else {
            systemInstruction = MindmapPromptTemplate.MINDMAP_JSON_INSTRUCTION;
        }

        // Call Gemini
        String assistantRaw = geminiService.generateWithSystem(systemInstruction, request.getPrompt());

        // Save assistant message
        Message assistantMessage = Message.builder()
                .conversation(conversation)
                .role("assistant")
                .content(assistantRaw)
                .build();
        messageRepository.save(assistantMessage);

        // Try to parse assistantRaw as JSON and extract nodes/edges
        Long generatedDataId = null;
        try {
            JsonNode root = objectMapper.readTree(assistantRaw);
            JsonNode nodesNode = root.has("nodes") ? root.get("nodes") : null;
            JsonNode edgesNode = root.has("edges") ? root.get("edges") : null;

            if (nodesNode != null || edgesNode != null) {
                GeneratedData gd = GeneratedData.builder()
                        .message(assistantMessage)
                        .nodes(nodesNode != null ? nodesNode.toString() : null)
                        .edges(edgesNode != null ? edgesNode.toString() : null)
                        .isChecked(false)
                        .build();
                generatedDataRepository.save(gd);
                generatedDataId = gd.getGeneratedDataId();
            }
        } catch (Exception ex) {
            // Not valid JSON or parse failed: still keep assistant message but don't persist generated data
            // Could log the error in real app
        }

        // Decrement quota
        entitlementService.decrementQuota(apiKey);

        return new ChatResponse(assistantRaw, conversationId, generatedDataId);
    }

    @Override
    public Conversation getConversation(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));
    }

    @Override
    public List<Conversation> getUserConversations(Long userId) {
        return conversationRepository.findByUserIdAndIsActiveOrderByUpdatedAtDesc(userId, true);
    }

    @Override
    public ConversationResponse getConversationWithMessages(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));
        return conversationMapper.toResponse(conversation);
    }

    @Override
    @Transactional
    public void deleteConversation(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));
        conversation.setIsActive(false);
        conversationRepository.save(conversation);
    }
}
