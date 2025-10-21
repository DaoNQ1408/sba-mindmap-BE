package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.PostRequest;
import com.sbaproject.sbamindmap.dto.response.PostResponse;
import com.sbaproject.sbamindmap.entity.Collection;
import com.sbaproject.sbamindmap.entity.Community;
import com.sbaproject.sbamindmap.entity.Mindmap;
import com.sbaproject.sbamindmap.entity.Post;
import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.mapper.PostMapper;
import com.sbaproject.sbamindmap.repository.CollectionRepository;
import com.sbaproject.sbamindmap.repository.CommunityRepository;
import com.sbaproject.sbamindmap.repository.MindmapRepository;
import com.sbaproject.sbamindmap.repository.PostRepository;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final PostMapper postMapper;

    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final MindmapRepository mindmapRepository;

    @Override
    @Transactional
    public PostResponse create(PostRequest request) {
        Community community = communityRepository.findById(request.getCommunityId())
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post entity = postMapper.toEntity(request);
        entity.setCommunity(community);
        entity.setUser(user);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return postMapper.toResponse(postRepository.save(entity));
    }

    @Override
    public List<PostResponse> getAll() {
        return postMapper.toResponseList(postRepository.findAll());
    }

    @Override
    public PostResponse getById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        return postMapper.toResponse(post);
    }

    @Override
    @Transactional
    public PostResponse update(Long id, PostRequest request) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setUpdatedAt(LocalDateTime.now());
        return postMapper.toResponse(postRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        post.getCollections().clear();
        post.getMindmaps().clear();
        postRepository.save(post);
        postRepository.delete(post);
    }
    @Override
    @Transactional
    public void addPostToCollection(Long postId, Long collectionId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        post.getCollections().add(collection);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void removePostFromCollection(Long postId, Long collectionId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found"));

        post.getCollections().remove(collection);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void addMindmapToPost(Long postId, Long mindmapId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        Mindmap mindmap = mindmapRepository.findById(mindmapId)
                .orElseThrow(() -> new EntityNotFoundException("Mindmap not found"));

        post.getMindmaps().add(mindmap);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void removeMindmapFromPost(Long postId, Long mindmapId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        Mindmap mindmap = mindmapRepository.findById(mindmapId)
                .orElseThrow(() -> new EntityNotFoundException("Mindmap not found"));

        post.getMindmaps().remove(mindmap);
        postRepository.save(post);
    }

}