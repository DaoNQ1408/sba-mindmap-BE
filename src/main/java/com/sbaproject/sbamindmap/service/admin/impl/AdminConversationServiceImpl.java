package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.entity.Conversation;
import com.sbaproject.sbamindmap.repository.ConversationRepository;
import com.sbaproject.sbamindmap.service.admin.AdminConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminConversationServiceImpl implements AdminConversationService {

    private final ConversationRepository conversationRepository;

    @Override
    public List<Conversation> getAll() {
        return conversationRepository.findAll();
    }

    @Override
    public Conversation getById(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    @Override
    public Conversation create(Conversation conversation) {
        conversation.setCreatedAt(Instant.now());
        conversation.setUpdatedAt(Instant.now());
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation update(Long id, Conversation request) {
        Conversation convo = getById(id);
        convo.setTitle(request.getTitle());
        convo.setIsActive(request.getIsActive());
        convo.setApiKey(request.getApiKey());
        convo.setUser(request.getUser());
        convo.setUpdatedAt(Instant.now());
        return conversationRepository.save(convo);
    }

    @Override
    public void delete(Long id) {
        if (!conversationRepository.existsById(id)) {
            throw new RuntimeException("Conversation not found");
        }
        conversationRepository.deleteById(id);
    }
}
