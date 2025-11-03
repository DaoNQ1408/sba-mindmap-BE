package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.entity.ApiKey;

import java.util.List;
import java.util.Map;

public interface AdminApiKeyService {
    List<ApiKey> getAll();
    ApiKey getById(Long id);
    ApiKey create(ApiKey apiKey);
    ApiKey update(Long id, ApiKey apiKey);
    void delete(Long id);
    Map<String, Object> getUsage(Long id);
}
