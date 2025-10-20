package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OdersRepository extends JpaRepository<Orders, Long> {
}
