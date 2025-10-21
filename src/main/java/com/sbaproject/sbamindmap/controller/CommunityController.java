package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.CommunityRequest;
import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.dto.response.CommunityResponse;
import com.sbaproject.sbamindmap.dto.response.UserResponse;
import com.sbaproject.sbamindmap.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommunityResponse>> create(@RequestBody CommunityRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.createCommunity(request), "Community created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommunityResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.getCommunityById(id), "Community retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommunityResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.getAllCommunities(), "All communities retrieved"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommunityResponse>> update(@PathVariable Long id,
                                                                 @RequestBody CommunityRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.updateCommunity(id, request), "Community updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        communityService.deleteCommunity(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Community deleted successfully"));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getMembers(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.getCommunityMembers(id), "Members retrieved successfully"));
    }

    @PostMapping("/{communityId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> addMember(@PathVariable Long communityId, @PathVariable Long userId) {
        communityService.addMemberToCommunity(communityId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Member added successfully"));
    }

    @DeleteMapping("/{communityId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(@PathVariable Long communityId, @PathVariable Long userId) {
        communityService.removeMemberFromCommunity(communityId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Member removed successfully"));
    }
}

