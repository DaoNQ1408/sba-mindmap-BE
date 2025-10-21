package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByName(String name);
}
