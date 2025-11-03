package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.entity.Community;
import com.sbaproject.sbamindmap.repository.CommunityRepository;
import com.sbaproject.sbamindmap.service.admin.AdminCommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommunityServiceImpl implements AdminCommunityService {

    private final CommunityRepository communityRepository;

    @Override
    public List<Community> getAll() {
        return communityRepository.findAll();
    }

    @Override
    public Community getById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    @Override
    public Community create(Community community) {
        community.setCreatedAt(LocalDateTime.now());
        community.setUpdatedAt(LocalDateTime.now());
        return communityRepository.save(community);
    }

    @Override
    public Community update(Long id, Community request) {
        Community community = getById(id);
        community.setName(request.getName());
        community.setStatus(request.getStatus());
        community.setUpdatedAt(LocalDateTime.now());
        return communityRepository.save(community);
    }

    @Override
    public void delete(Long id) {
        if (!communityRepository.existsById(id)) {
            throw new RuntimeException("Community not found");
        }
        communityRepository.deleteById(id);
    }
}
