package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.repository.*;
import com.sbaproject.sbamindmap.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final PackagesRepository packagesRepository;
    private final TemplateRepository templateRepository;
    private final OderRepository ordersRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final CommunityRepository communityRepository;
    private final PostRepository postRepository;
    private final ConversationRepository conversationRepository;

    @Override
    public Map<String, Object> getSystemStats() {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("totalUsers", userRepository.count());
        data.put("totalPackages", packagesRepository.count());
        data.put("totalTemplates", templateRepository.count());
        data.put("totalTransactions", ordersRepository.count());
        data.put("totalApiKeys", apiKeyRepository.count());
        data.put("totalCommunities", communityRepository.count());
        data.put("totalPosts", postRepository.count());
        data.put("totalConversations", conversationRepository.count());

        long activeApiKeys = apiKeyRepository.findAll().stream()
                .filter(key -> Boolean.TRUE.equals(key.getIsActive()))
                .count();
        data.put("activeApiKeys", activeApiKeys);

        long inactiveUsers = userRepository.findAll().stream()
                .filter(u -> u.getUserStatus() != null && u.getUserStatus().name().equalsIgnoreCase("INACTIVE"))
                .count();
        data.put("inactiveUsers", inactiveUsers);

        return data;
    }
}
