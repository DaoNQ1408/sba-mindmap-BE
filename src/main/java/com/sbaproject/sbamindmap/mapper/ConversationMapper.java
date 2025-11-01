package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.response.ConversationResponse;
import com.sbaproject.sbamindmap.dto.response.GeneratedDataResponse;
import com.sbaproject.sbamindmap.dto.response.MessageResponse;
import com.sbaproject.sbamindmap.entity.Conversation;
import com.sbaproject.sbamindmap.entity.GeneratedData;
import com.sbaproject.sbamindmap.entity.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConversationMapper {

    public ConversationResponse toResponse(Conversation conversation) {
        return ConversationResponse.builder()
                .conversationId(conversation.getConversationId())
                .title(conversation.getTitle())
                .isActive(conversation.getIsActive())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .userId(conversation.getUser() != null ? conversation.getUser().getId() : null)
                .apiKeyId(conversation.getApiKey() != null ? conversation.getApiKey().getApiKeyId() : null)
                .messages(conversation.getMessages() != null
                    ? conversation.getMessages().stream().map(this::toMessageResponse).collect(Collectors.toList())
                    : null)
                .build();
    }

    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .messageId(message.getMessageId())
                .role(message.getRole())
                .content(message.getContent())
                .metadata(message.getMetadata())
                .createdAt(message.getCreatedAt())
                .build();
    }

    public GeneratedDataResponse toGeneratedDataResponse(GeneratedData data) {
        if (data == null) return null;
        return GeneratedDataResponse.builder()
                .generatedDataId(data.getGeneratedDataId())
                .nodes(data.getNodes())
                .edges(data.getEdges())
                .isChecked(data.getIsChecked())
                .createdAt(data.getCreatedAt())
                .build();
    }
}

