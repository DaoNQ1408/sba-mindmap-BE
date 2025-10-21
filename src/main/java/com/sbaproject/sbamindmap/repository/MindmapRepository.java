package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.Mindmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MindmapRepository extends JpaRepository<Mindmap, Long> {
    Optional<Object> findByName(String name);
}
