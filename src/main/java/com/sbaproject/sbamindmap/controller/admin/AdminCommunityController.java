package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.Community;
import com.sbaproject.sbamindmap.service.admin.AdminCommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/communities")
@RequiredArgsConstructor
public class AdminCommunityController {

    private final AdminCommunityService adminCommunityService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Community>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminCommunityService.getAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Community>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminCommunityService.getById(id), "Success"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Community>> create(@RequestBody Community community) {
        return ResponseEntity.ok(ApiResponse.success(adminCommunityService.create(community), "Community created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Community>> update(@PathVariable Long id, @RequestBody Community request) {
        return ResponseEntity.ok(ApiResponse.success(adminCommunityService.update(id, request), "Community updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        adminCommunityService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Community deleted successfully"));
    }
}
