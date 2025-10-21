package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.entity.ApiKey;
import com.sbaproject.sbamindmap.repository.ApiKeyRepository;
import com.sbaproject.sbamindmap.service.EntitlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EntitlementServiceImpl implements EntitlementService {
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    /**
     * Lấy tất cả các API key còn hiệu lực của user
     */
    @Override
    public List<ApiKey> getAvailableKeys(Long userId) {
        return apiKeyRepository.findAvailableKeys(userId, Instant.now());//        return apiKeyRepository.findAvailableKeys(userId, Instant.now());
    }

    /**
     * Kiểm tra 1 key cụ thể user có được quyền xài không
     */
    @Override
    public boolean isKeyUsable(Long userId, Long apiKeyId) {
        return apiKeyRepository.findAvailableKeys(userId, Instant.now())
                .stream().anyMatch(k -> k.getApiKeyId().equals(apiKeyId));
    }

    /**
     * Giảm quota (remaining_calls - 1)
     */
    @Override
    public void decrementQuota(ApiKey key) {
        key.setRemainingCalls(key.getRemainingCalls() - 1);
        if (key.getRemainingCalls() <= 0) key.setIsActive(false);
        apiKeyRepository.save(key);
    }
}
