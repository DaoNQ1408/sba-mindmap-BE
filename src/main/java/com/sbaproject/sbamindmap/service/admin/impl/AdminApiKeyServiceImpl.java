package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.dto.request.ApiKeyRequest;
import com.sbaproject.sbamindmap.entity.ApiKey;
import com.sbaproject.sbamindmap.entity.Packages;
import com.sbaproject.sbamindmap.repository.ApiKeyRepository;
import com.sbaproject.sbamindmap.repository.PackagesRepository;
import com.sbaproject.sbamindmap.service.admin.AdminApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminApiKeyServiceImpl implements AdminApiKeyService {
    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    private PackagesRepository packagesRepository;

    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;

    @Value("${OPENAI_API_KEY}")
    private String openaiApiKey;

    @Override
    public List<ApiKey> getAll() {
        return apiKeyRepository.findAll();
    }

    @Override
    public ApiKey getById(Long id) {
        return apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API key not found"));
    }

    @Override
    public ApiKey create(ApiKeyRequest apiKey, Long packageID) {
        ApiKey newApi = new ApiKey();
        newApi.setKeyValue(openaiApiKey);
        newApi.setRemainingCalls(apiKey.getRemainingCalls());
        newApi.setIsActive(apiKey.getIsActive());
        newApi.setActivatedAt(apiKey.getActivatedAt());
        newApi.setExpiredAt(apiKey.getExpiredAt());
        newApi.setAPackage(getPackageById(packageID));
        return apiKeyRepository.save(newApi);
    }

    @Override
    public Packages getPackageById(Long packageId) {
        return packagesRepository.findById(packageId).orElse(null);
    }






    @Override
    public ApiKey update(Long id, ApiKey request) {
        ApiKey key = getById(id);
        key.setRemainingCalls(request.getRemainingCalls());
        key.setIsActive(request.getIsActive());
        key.setActivatedAt(request.getActivatedAt());
        key.setExpiredAt(request.getExpiredAt());
        key.setAPackage(request.getAPackage());
        return apiKeyRepository.save(key);
    }

    @Override
    public void delete(Long id) {
        if (!apiKeyRepository.existsById(id)) {
            throw new RuntimeException("API key not found");
        }
        apiKeyRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> getUsage(Long id) {
        ApiKey key = getById(id);
        Map<String, Object> usage = new LinkedHashMap<>();
        usage.put("keyValue", key.getKeyValue());
        usage.put("remainingCalls", key.getRemainingCalls());
        usage.put("isActive", key.getIsActive());
        usage.put("activatedAt", key.getActivatedAt());
        usage.put("expiredAt", key.getExpiredAt());
        return usage;
    }
}
