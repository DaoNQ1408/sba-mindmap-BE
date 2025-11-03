package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.Conversation;
import com.sbaproject.sbamindmap.service.admin.AdminConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/conversations")
@RequiredArgsConstructor
public class AdminConversationController {

    private final AdminConversationService adminConversationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Conversation>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminConversationService.getAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Conversation>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminConversationService.getById(id), "Success"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Conversation>> create(@RequestBody Conversation conv) {
        return ResponseEntity.ok(ApiResponse.success(adminConversationService.create(conv), "Conversation created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Conversation>> update(@PathVariable Long id, @RequestBody Conversation request) {
        return ResponseEntity.ok(ApiResponse.success(adminConversationService.update(id, request), "Conversation updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        adminConversationService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Conversation deleted successfully"));
    }
}
