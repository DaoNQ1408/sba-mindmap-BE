package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
}
