package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.entity.Community;

import java.util.List;

public interface AdminCommunityService {
    List<Community> getAll();
    Community getById(Long id);
    Community create(Community community);
    Community update(Long id, Community community);
    void delete(Long id);
}
