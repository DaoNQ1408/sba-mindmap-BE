package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Optional<Object> findByName(String collectionName);
}
