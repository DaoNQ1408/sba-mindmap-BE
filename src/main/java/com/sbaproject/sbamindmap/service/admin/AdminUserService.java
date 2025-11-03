package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.entity.User;

import java.util.List;

public interface AdminUserService {
    List<User> getAll();
    User getById(Long id);
    User create(User user);
    User update(Long id, User request);
    void delete(Long id);
}
