package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.entity.Conversation;

import java.util.List;

public interface AdminConversationService {
    List<Conversation> getAll();
    Conversation getById(Long id);
    Conversation create(Conversation conversation);
    Conversation update(Long id, Conversation conversation);
    void delete(Long id);
}
