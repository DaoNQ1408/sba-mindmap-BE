package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.entity.Post;
import com.sbaproject.sbamindmap.repository.PostRepository;
import com.sbaproject.sbamindmap.service.admin.AdminPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPostServiceImpl implements AdminPostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public Post create(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Override
    public Post update(Long id, Post request) {
        Post post = getById(id);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCommunity(request.getCommunity());
        post.setUser(request.getUser());
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found");
        }
        postRepository.deleteById(id);
    }
}
