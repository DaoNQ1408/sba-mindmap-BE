package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.CommunityRequest;
import com.sbaproject.sbamindmap.dto.response.CommunityResponse;
import com.sbaproject.sbamindmap.entity.Community;
import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.mapper.CommunityMapper;
import com.sbaproject.sbamindmap.repository.CommunityRepository;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.service.CommunityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final CommunityMapper mapper;

    @Override
    @Transactional
    public CommunityResponse createCommunity(CommunityRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Community community = mapper.toEntity(request);
        community.setUser(user);
        community.setCreatedAt(LocalDateTime.now());
        community.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(communityRepository.save(community));
    }

    @Override
    public CommunityResponse getCommunityById(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        return mapper.toResponse(community);
    }

    @Override
    public List<CommunityResponse> getAllCommunities() {
        return communityRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CommunityResponse updateCommunity(Long id, CommunityRequest request) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        community.setName(request.getName());
        community.setStatus(request.getStatus());
        community.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(communityRepository.save(community));
    }

    @Override
    @Transactional
    public void deleteCommunity(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        community.getMembers().clear();
        communityRepository.save(community);
        communityRepository.delete(community);
    }

    @Override
    @Transactional
    public void addMemberToCommunity(Long communityId, Long userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        community.getMembers().add(user);
        communityRepository.save(community);
    }

    @Override
    @Transactional
    public void removeMemberFromCommunity(Long communityId, Long userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        community.getMembers().remove(user);
        communityRepository.save(community);
    }

    @Override
    public List<UserResponse> getCommunityMembers(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        return community.getMembers().stream()
                .map(userMapper::toResponse)
                .toList();
    }
}

