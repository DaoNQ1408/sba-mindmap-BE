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
import com.sbaproject.sbamindmap.exception.DuplicateObjectException;
import com.sbaproject.sbamindmap.repository.*;
import com.sbaproject.sbamindmap.service.ChatService;
import com.sbaproject.sbamindmap.util.MathTopicValidator;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final GeneratedDataRepository generatedDataRepository;
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final MathTopicValidator mathTopicValidator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    @Override
    public ChatMessageResponse generateMindmap(GenerateMindmapRequest request) {
        log.info("🚀 Generating mindmap - Topic: '{}', Grade: {}", request.getTopic(), request.getGrade());

        // 1. Validate topic & grade
        MathTopicValidator.ValidationResult validation =
                mathTopicValidator.validate(request.getTopic(), request.getGrade());

        // 2. Tìm hoặc tạo user
        User user = getOrCreateTestUser(request.getUserId());

        // 3. Tìm hoặc tạo conversation
        Conversation conversation = getOrCreateConversation(request.getConversationId(), user, request.getTopic());

        // 4. Lưu user message
        String userPrompt = String.format(OpenAIPromptTemplate.USER_PROMPT_TEMPLATE,
                request.getGrade(), request.getTopic());
        Message userMessage = createAndSaveMessage(conversation, "user", userPrompt);

        // 5. Nếu validation thất bại -> trả lời từ chối
        if (!validation.isValid()) {
            log.warn("❌ Invalid math topic or grade: {}", validation.getErrorMessage());

            String rejectionMessage = validation.getErrorMessage();
            Message assistantMessage = createAndSaveMessage(conversation, "assistant", rejectionMessage);

            conversation.setUpdatedAt(Instant.now());
            conversationRepository.save(conversation);

            return buildChatMessageResponse(assistantMessage, null);
        }

        // 6. Lấy API key từ DB
        ApiKey apiKey = getActiveApiKey(user.getId());

        // 7. Gọi OpenAI API
        String aiResponse = callOpenAI(conversation, apiKey.getKeyValue(),
                OpenAIPromptTemplate.MINDMAP_SYSTEM_INSTRUCTION, userPrompt);

        // 8. Validate và parse JSON (bao gồm knowledge)
        MindmapJsonData mindmapData = validateAndParseMindmapJson(aiResponse);

        // 9. Lưu assistant message
        Message assistantMessage = createAndSaveMessage(conversation, "assistant", aiResponse);

        // 10. Lưu generated data với knowledgeJson
        GeneratedData generatedData = GeneratedData.builder()
                .message(assistantMessage)
                .nodes(mindmapData.nodesJson)
                .edges(mindmapData.edgesJson)
                .knowledgeJson(mindmapData.knowledgeJson)
                .isChecked(false)
                .build();
        generatedDataRepository.save(generatedData);

        // 11. Giảm remaining_calls
        apiKey.setRemainingCalls(apiKey.getRemainingCalls() - 1);
        apiKeyRepository.save(apiKey);

        // 12. Update conversation
        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);

        log.info("✅ Mindmap generated successfully - ConversationId: {}, GeneratedDataId: {}",
                conversation.getConversationId(), generatedData.getGeneratedDataId());

        return buildChatMessageResponse(assistantMessage, generatedData);
    }

    @Override
    public ChatMessageResponse chat(ChatRequest request) {
        log.info("💬 Processing chat message for user: {}", request.getUserId());

        // 1. Validate nội dung có phải Toán không
        MathTopicValidator.ValidationResult validation =
                mathTopicValidator.validate(request.getMessage(), "10"); // Giả sử grade mặc định

        // 2. Tìm hoặc tạo user
        User user = getOrCreateTestUser(request.getUserId());

        // 3. Tìm hoặc tạo conversation
        Conversation conversation = getOrCreateConversation(request.getConversationId(), user, "General Chat");

        // 4. Lưu user message
        Message userMessage = createAndSaveMessage(conversation, "user", request.getMessage());

        String aiResponse;

        // 5. Nếu không hợp lệ -> từ chối
        if (!validation.isValid()) {
            log.warn("❌ Invalid chat topic: {}", validation.getErrorMessage());
            aiResponse = validation.getErrorMessage();
        } else {
            // 6. Lấy API key & gọi OpenAI
            ApiKey apiKey = getActiveApiKey(user.getId());
            aiResponse = callOpenAI(conversation, apiKey.getKeyValue(), null, request.getMessage());

            // Giảm remaining_calls
            apiKey.setRemainingCalls(apiKey.getRemainingCalls() - 1);
            apiKeyRepository.save(apiKey);
        }

        // 7. Lưu assistant message
        Message assistantMessage = createAndSaveMessage(conversation, "assistant", aiResponse);

        // 8. Update conversation
        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);

        return buildChatMessageResponse(assistantMessage, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getConversationHistory(Long conversationId) {
        log.info("📜 Fetching conversation history for ID: {}", conversationId);

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found with ID: " + conversationId));

        List<Message> messages = messageRepository
                .findByConversationConversationIdOrderByCreatedAtAsc(conversationId);

        return messages.stream()
                .map(msg -> {
                    GeneratedData genData = generatedDataRepository.findAll().stream()
                            .filter(gd -> gd.getMessage() != null &&
                                    gd.getMessage().getMessageId().equals(msg.getMessageId()))
                            .findFirst()
                            .orElse(null);
                    return buildChatMessageResponse(msg, genData);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationResponse> getUserConversations(String userId) {
        log.info("📋 Fetching conversations for user: {}", userId);

        User user = userRepository.findById(parseUserId(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<Conversation> conversations = conversationRepository
                .findByUserIdAndIsActiveOrderByUpdatedAtDesc(user.getId(), true);

        return conversations.stream()
                .map(this::buildConversationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ConversationResponse createConversation(String userId, String title) {
        log.info("➕ Creating new conversation for user: {} with title: {}", userId, title);

        User user = getOrCreateTestUser(userId);

        Conversation conversation = Conversation.builder()
                .user(user)
                .title(title != null ? title : "New Conversation")
                .isActive(true)
                .build();

        conversationRepository.save(conversation);

        return buildConversationResponse(conversation);
    }

    // ============ HELPER METHODS ============

    private User getOrCreateTestUser(String userId) {
        Long id = parseUserId(userId);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
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
                    .orElseThrow(() -> new RuntimeException("Conversation not found with ID: " + conversationId));
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

    private ApiKey getActiveApiKey(Long userId) {
        List<ApiKey> availableKeys = apiKeyRepository.findAvailableKeys(userId, Instant.now());

        if (availableKeys.isEmpty()) {
            throw new RuntimeException("No active API key found for user. Please purchase a package.");
        }

        return availableKeys.get(0);
    }

    private String callOpenAI(Conversation conversation, String apiKeyValue,
                             String systemInstruction, String userMessage) {
        // Tạo OpenAI service với API key từ DB
        OpenAiService openAiService = new OpenAiService(apiKeyValue, Duration.ofSeconds(60));

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
                .maxTokens(3000)
                .build();

        try {
            String response = openAiService.createChatCompletion(completionRequest)
                    .getChoices().get(0).getMessage().getContent();

            openAiService.shutdownExecutor();
            return response;
        } catch (Exception e) {
            log.error("❌ Error calling OpenAI API", e);
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
            JsonNode knowledgeNode = rootNode.get("knowledge");

            if (!nodesNode.isArray() || !edgesNode.isArray()) {
                throw new RuntimeException("Invalid mindmap JSON: 'nodes' and 'edges' must be arrays");
            }

            // Chuyển knowledge thành JSON string (có thể null)
            String knowledgeJson = null;
            if (knowledgeNode != null && !knowledgeNode.isNull()) {
                knowledgeJson = objectMapper.writeValueAsString(knowledgeNode);
            }

            // Trả về JSON string riêng biệt
            return new MindmapJsonData(
                    objectMapper.writeValueAsString(nodesNode),
                    objectMapper.writeValueAsString(edgesNode),
                    knowledgeJson
            );
        } catch (Exception e) {
            log.error("❌ Error parsing mindmap JSON", e);
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
                    .knowledgeJson(generatedData.getKnowledgeJson())
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

    // Inner class để giữ nodes, edges và knowledge JSON
    private static class MindmapJsonData {
        String nodesJson;
        String edgesJson;
        String knowledgeJson;

        MindmapJsonData(String nodesJson, String edgesJson, String knowledgeJson) {
            this.nodesJson = nodesJson;
            this.edgesJson = edgesJson;
            this.knowledgeJson = knowledgeJson;
        }
    }
}

