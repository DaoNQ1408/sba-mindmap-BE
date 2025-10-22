package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.entity.ApiKey;

import java.util.List;

public interface EntitlementService {
    List<ApiKey> getAvailableKeys(Long userId);
    boolean isKeyUsable(Long userId, Long apiKeyId);
    void decrementQuota(ApiKey key);
}
