package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.PostRequest;
import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.dto.response.PostResponse;
import com.sbaproject.sbamindmap.service.PostService;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(@RequestBody PostRequest request) {
        return ResponseEntity.ok(ApiResponse.success(postService.create(request),"Post created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(postService.getAll(),"Posts fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(postService.getById(id),"Post fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(@PathVariable Long id, @RequestBody PostRequest request) {
        return ResponseEntity.ok(ApiResponse.success( postService.update(id, request),"Post updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null,"Post deleted successfully"));
    }

    // --- ENDPOINTS THÊM MỚI (COLLECTIONS) ---

    @PostMapping("/{postId}/collections/{collectionId}")
    public ResponseEntity<ApiResponse<Void>> addPostToCollection(@PathVariable Long postId, @PathVariable Long collectionId) {
        postService.addPostToCollection(postId, collectionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Post added to collection"));
    }

    @DeleteMapping("/{postId}/collections/{collectionId}")
    public ResponseEntity<ApiResponse<Void>> removePostFromCollection(@PathVariable Long postId, @PathVariable Long collectionId) {
        postService.removePostFromCollection(postId, collectionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Post removed from collection"));
    }

    // --- ENDPOINTS THÊM MỚI (MINDMAPS) ---

    @PostMapping("/{postId}/mindmaps/{mindmapId}")
    public ResponseEntity<ApiResponse<Void>> addMindmapToPost(@PathVariable Long postId, @PathVariable Long mindmapId) {
        postService.addMindmapToPost(postId, mindmapId);
        return ResponseEntity.ok(ApiResponse.success(null, "Mindmap added to post"));
    }

    @DeleteMapping("/{postId}/mindmaps/{mindmapId}")
    public ResponseEntity<ApiResponse<Void>> removeMindmapFromPost(@PathVariable Long postId, @PathVariable Long mindmapId) {
        postService.removeMindmapFromPost(postId, mindmapId);
        return ResponseEntity.ok(ApiResponse.success(null, "Mindmap removed from post"));
    }
}