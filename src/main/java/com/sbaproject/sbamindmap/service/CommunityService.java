package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.CommunityRequest;
import com.sbaproject.sbamindmap.dto.response.CommunityResponse;

import java.util.List;

public interface CommunityService {
    CommunityResponse createCommunity(CommunityRequest request);
    CommunityResponse getCommunityById(Long id);
    List<CommunityResponse> getAllCommunities();
    CommunityResponse updateCommunity(Long id, CommunityRequest request);
    void deleteCommunity(Long id);
    void addMemberToCommunity(Long communityId, Long userId);
    void removeMemberFromCommunity(Long communityId, Long userId);
    List<UserResponse> getCommunityMembers(Long communityId);
}
