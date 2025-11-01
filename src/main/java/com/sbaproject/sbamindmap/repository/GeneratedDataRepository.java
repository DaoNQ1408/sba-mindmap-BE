package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.GeneratedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneratedDataRepository extends JpaRepository<GeneratedData, Long> {
}

