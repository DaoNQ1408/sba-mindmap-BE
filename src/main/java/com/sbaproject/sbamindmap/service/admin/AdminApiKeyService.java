package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.dto.request.ApiKeyRequest;
import com.sbaproject.sbamindmap.entity.ApiKey;
import com.sbaproject.sbamindmap.entity.Packages;

import java.util.List;
import java.util.Map;

public interface AdminApiKeyService {
    List<ApiKey> getAll();
    ApiKey getById(Long id);
    ApiKey create(ApiKeyRequest apiKey, Long packageId);
    ApiKey update(Long id, ApiKey apiKey);
    void delete(Long id);
    Map<String, Object> getUsage(Long id);
    Packages getPackageById(Long packageId);
}
