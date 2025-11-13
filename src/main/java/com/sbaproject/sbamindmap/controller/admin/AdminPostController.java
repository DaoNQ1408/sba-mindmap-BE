package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.Post;
import com.sbaproject.sbamindmap.service.admin.AdminPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService adminPostService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Post>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminPostService.getAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminPostService.getById(id), "Success"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Post>> create(@RequestBody Post post) {
        return ResponseEntity.ok(ApiResponse.success(adminPostService.create(post), "Post created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> update(@PathVariable Long id, @RequestBody Post request) {
        return ResponseEntity.ok(ApiResponse.success(adminPostService.update(id, request), "Post updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        adminPostService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Post deleted successfully"));
    }
}
