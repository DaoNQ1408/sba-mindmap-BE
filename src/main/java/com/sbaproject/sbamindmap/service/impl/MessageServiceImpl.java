package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.entity.Message;
import com.sbaproject.sbamindmap.repository.MessageRepository;
import com.sbaproject.sbamindmap.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public List<Message> getMessagesByConversation(Long conversationId) {
        return messageRepository.findByConversationConversationIdOrderByCreatedAtAsc(conversationId);
    }
}

