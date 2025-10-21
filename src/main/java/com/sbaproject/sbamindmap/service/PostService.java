package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.PostRequest;
import com.sbaproject.sbamindmap.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse create(PostRequest request);
    List<PostResponse> getAll();
    PostResponse getById(Long id);
    PostResponse update(Long id, PostRequest request);
    void delete(Long id);
    // Quản lý Collections
    void addPostToCollection(Long postId, Long collectionId);
    void removePostFromCollection(Long postId, Long collectionId);
    // Quản lý Mindmaps
    void addMindmapToPost(Long postId, Long mindmapId);
    void removeMindmapFromPost(Long postId, Long mindmapId);
}