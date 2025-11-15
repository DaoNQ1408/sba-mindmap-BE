package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.entity.Message;

import java.util.List;

public interface MessageService {

    /**
     * Lấy tất cả messages của một conversation theo thứ tự thời gian tăng dần
     */
    List<Message> getMessagesByConversation(Long conversationId);
}
