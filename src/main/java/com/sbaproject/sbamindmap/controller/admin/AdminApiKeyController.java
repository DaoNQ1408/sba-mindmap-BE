package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.request.ApiKeyRequest;
import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.ApiKey;
import com.sbaproject.sbamindmap.service.admin.AdminApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/apikeys")
@RequiredArgsConstructor
public class AdminApiKeyController {

    private final AdminApiKeyService adminApiKeyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApiKey>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminApiKeyService.getAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApiKey>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminApiKeyService.getById(id), "Success"));
    }

    @GetMapping("/{id}/usage")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUsage(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminApiKeyService.getUsage(id), "Usage info fetched successfully"));
    }
 
    @PostMapping("{packageId}")
    public ResponseEntity<ApiResponse<ApiKey>> create(@RequestBody ApiKeyRequest request,@PathVariable Long packageId) {
        return ResponseEntity.ok(ApiResponse.success(adminApiKeyService.create(request, packageId), "API key created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ApiKey>> update(@PathVariable Long id, @RequestBody ApiKey request) {
        return ResponseEntity.ok(ApiResponse.success(adminApiKeyService.update(id, request), "API key updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        adminApiKeyService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "API key deleted successfully"));
    }
}
