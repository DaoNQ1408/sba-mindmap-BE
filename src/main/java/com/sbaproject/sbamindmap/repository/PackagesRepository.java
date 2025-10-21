package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagesRepository extends JpaRepository <Packages, Long>{
}
