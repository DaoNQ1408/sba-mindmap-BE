package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.entity.Post;

import java.util.List;

public interface AdminPostService {
    List<Post> getAll();
    Post getById(Long id);
    Post create(Post post);
    Post update(Long id, Post post);
    void delete(Long id);
}
