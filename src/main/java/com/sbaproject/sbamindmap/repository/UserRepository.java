package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
