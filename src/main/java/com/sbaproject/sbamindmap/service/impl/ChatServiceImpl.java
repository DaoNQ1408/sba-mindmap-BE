package com.sbaproject.sbamindmap.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbaproject.sbamindmap.constant.OpenAIPromptTemplate;
import com.sbaproject.sbamindmap.dto.request.ChatRequest;
import com.sbaproject.sbamindmap.dto.request.GenerateMindmapRequest;
import com.sbaproject.sbamindmap.dto.response.ChatMessageResponse;
import com.sbaproject.sbamindmap.dto.response.ConversationResponse;
import com.sbaproject.sbamindmap.dto.response.MindmapDataResponse;
import com.sbaproject.sbamindmap.entity.*;
import com.sbaproject.sbamindmap.repository.*;
import com.sbaproject.sbamindmap.service.ChatService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatServiceImpl implements ChatService {

    private final OpenAiService openAiService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final GeneratedDataRepository generatedDataRepository;
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    @Override
    public ChatMessageResponse generateMindmap(GenerateMindmapRequest request) {
        log.info("Generating mindmap for topic: {}, grade: {}", request.getTopic(), request.getGrade());

        // 1. Tìm hoặc tạo user giả lập
        User user = getOrCreateTestUser(request.getUserId());

        // 2. Tìm hoặc tạo conversation
        Conversation conversation = getOrCreateConversation(request.getConversationId(), user, request.getTopic());

        // 3. Tìm template nếu có
        Template template = null;
        if (request.getTemplateId() != null) {
            template = templateRepository.findById(request.getTemplateId())
                    .orElseThrow(() -> new RuntimeException("Template not found"));
        }

        // 4. Tạo user message
        String userPrompt = String.format(OpenAIPromptTemplate.USER_PROMPT_TEMPLATE,
                request.getGrade(), request.getTopic());

        Message userMessage = createAndSaveMessage(conversation, "user", userPrompt);

        // 5. Gọi OpenAI API
        String aiResponse = callOpenAI(conversation, OpenAIPromptTemplate.MINDMAP_SYSTEM_INSTRUCTION, userPrompt);

        // 6. Validate và parse JSON
        MindmapJsonData mindmapData = validateAndParseMindmapJson(aiResponse);

        // 7. Lưu assistant message
        Message assistantMessage = createAndSaveMessage(conversation, "assistant", aiResponse);

        // 8. Lưu generated data
        GeneratedData generatedData = GeneratedData.builder()
                .message(assistantMessage)
                .nodes(mindmapData.nodesJson)
                .edges(mindmapData.edgesJson)
                .isChecked(false)
                .build();
        generatedDataRepository.save(generatedData);

        // 9. Update conversation
        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);

        log.info("Mindmap generated successfully for conversation: {}", conversation.getConversationId());

        return buildChatMessageResponse(assistantMessage, generatedData);
    }

    @Override
    public ChatMessageResponse chat(ChatRequest request) {
        log.info("Processing chat message for user: {}", request.getUserId());

        // 1. Tìm hoặc tạo user
        User user = getOrCreateTestUser(request.getUserId());

        // 2. Tìm hoặc tạo conversation
        Conversation conversation = getOrCreateConversation(request.getConversationId(), user, "General Chat");

        // 3. Lưu user message
        Message userMessage = createAndSaveMessage(conversation, "user", request.getMessage());

        // 4. Gọi OpenAI API
        String aiResponse = callOpenAI(conversation, null, request.getMessage());

        // 5. Lưu assistant message
        Message assistantMessage = createAndSaveMessage(conversation, "assistant", aiResponse);

        // 6. Update conversation
        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);

        return buildChatMessageResponse(assistantMessage, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getConversationHistory(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationConversationIdOrderByCreatedAtAsc(conversationId);

        return messages.stream()
                .map(msg -> {
                    GeneratedData genData = generatedDataRepository.findAll().stream()
                            .filter(gd -> gd.getMessage().getMessageId().equals(msg.getMessageId()))
                            .findFirst()
                            .orElse(null);
                    return buildChatMessageResponse(msg, genData);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationResponse> getUserConversations(String userId) {
        User user = userRepository.findById(parseUserId(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Conversation> conversations = conversationRepository
                .findByUserIdAndIsActiveOrderByUpdatedAtDesc(user.getId(), true);

        return conversations.stream()
                .map(this::buildConversationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ConversationResponse createConversation(String userId, String title) {
        User user = getOrCreateTestUser(userId);

        Conversation conversation = Conversation.builder()
                .user(user)
                .title(title)
                .isActive(true)
                .build();

        conversationRepository.save(conversation);

        return buildConversationResponse(conversation);
    }

    // ============ HELPER METHODS ============

    private User getOrCreateTestUser(String userId) {
        Long id = parseUserId(userId);
        return userRepository.findById(id)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .id(id)
                            .username("test-user-" + id)
                            .mail("testuser" + id + "@example.com")
                            .fullName("Test User " + id)
                            .build();
                    return userRepository.save(newUser);
                });
    }

    private Long parseUserId(String userId) {
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return 1L; // Default test user ID
        }
    }

    private Conversation getOrCreateConversation(Long conversationId, User user, String defaultTitle) {
        if (conversationId != null) {
            return conversationRepository.findById(conversationId)
                    .orElseThrow(() -> new RuntimeException("Conversation not found"));
        }

        // Tạo conversation mới
        Conversation conversation = Conversation.builder()
                .user(user)
                .title(defaultTitle)
                .isActive(true)
                .messages(new ArrayList<>())
                .build();

        return conversationRepository.save(conversation);
    }

    private Message createAndSaveMessage(Conversation conversation, String role, String content) {
        Message message = Message.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .build();

        return messageRepository.save(message);
    }

    private String callOpenAI(Conversation conversation, String systemInstruction, String userMessage) {
        List<Message> history = messageRepository
                .findByConversationConversationIdOrderByCreatedAtAsc(conversation.getConversationId());

        List<ChatMessage> chatMessages = new ArrayList<>();

        // Thêm system instruction nếu có
        if (systemInstruction != null && !systemInstruction.isEmpty()) {
            chatMessages.add(new ChatMessage("system", systemInstruction));
        }

        // Thêm lịch sử hội thoại (giới hạn 10 tin nhắn gần nhất)
        int startIndex = Math.max(0, history.size() - 10);
        for (int i = startIndex; i < history.size(); i++) {
            Message msg = history.get(i);
            chatMessages.add(new ChatMessage(msg.getRole(), msg.getContent()));
        }

        // Thêm tin nhắn hiện tại
        chatMessages.add(new ChatMessage("user", userMessage));

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(chatMessages)
                .temperature(0.7)
                .maxTokens(2000)
                .build();

        try {
            return openAiService.createChatCompletion(completionRequest)
                    .getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.error("Error calling OpenAI API", e);
            throw new RuntimeException("Failed to get response from OpenAI: " + e.getMessage());
        }
    }

    private MindmapJsonData validateAndParseMindmapJson(String jsonResponse) {
        try {
            // Làm sạch response (loại bỏ markdown code blocks nếu có)
            String cleanJson = jsonResponse.trim();
            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substring(7);
            }
            if (cleanJson.startsWith("```")) {
                cleanJson = cleanJson.substring(3);
            }
            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
            }
            cleanJson = cleanJson.trim();

            // Parse JSON
            JsonNode rootNode = objectMapper.readTree(cleanJson);

            // Validate structure
            if (!rootNode.has("nodes") || !rootNode.has("edges")) {
                throw new RuntimeException("Invalid mindmap JSON: missing 'nodes' or 'edges'");
            }

            JsonNode nodesNode = rootNode.get("nodes");
            JsonNode edgesNode = rootNode.get("edges");

            if (!nodesNode.isArray() || !edgesNode.isArray()) {
                throw new RuntimeException("Invalid mindmap JSON: 'nodes' and 'edges' must be arrays");
            }

            // Trả về JSON string riêng biệt
            return new MindmapJsonData(
                    objectMapper.writeValueAsString(nodesNode),
                    objectMapper.writeValueAsString(edgesNode)
            );
        } catch (Exception e) {
            log.error("Error parsing mindmap JSON", e);
            throw new RuntimeException("Invalid mindmap JSON format: " + e.getMessage());
        }
    }

    private ChatMessageResponse buildChatMessageResponse(Message message, GeneratedData generatedData) {
        MindmapDataResponse mindmapDataResponse = null;

        if (generatedData != null) {
            mindmapDataResponse = MindmapDataResponse.builder()
                    .generatedDataId(generatedData.getGeneratedDataId())
                    .messageId(message.getMessageId())
                    .conversationId(message.getConversation().getConversationId())
                    .nodesJson(generatedData.getNodes())
                    .edgesJson(generatedData.getEdges())
                    .isChecked(generatedData.getIsChecked())
                    .createdAt(generatedData.getCreatedAt())
                    .build();
        }

        return ChatMessageResponse.builder()
                .messageId(message.getMessageId())
                .conversationId(message.getConversation().getConversationId())
                .role(message.getRole())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .generatedData(mindmapDataResponse)
                .build();
    }

    private ConversationResponse buildConversationResponse(Conversation conversation) {
        return ConversationResponse.builder()
                .conversationId(conversation.getConversationId())
                .title(conversation.getTitle())
                .isActive(conversation.getIsActive())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .messageCount(conversation.getMessages() != null ? conversation.getMessages().size() : 0)
                .build();
    }

    // Inner class để giữ nodes và edges JSON
    private static class MindmapJsonData {
        String nodesJson;
        String edgesJson;

        MindmapJsonData(String nodesJson, String edgesJson) {
            this.nodesJson = nodesJson;
            this.edgesJson = edgesJson;
        }
    }
}

