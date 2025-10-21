package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    @Query("""
       SELECT k FROM ApiKey k
       WHERE k.user.id = :userId
         AND k.isActive = true
         AND (k.expiredAt IS NULL OR k.expiredAt > :now)
         AND k.remainingCalls > 0
         AND EXISTS (
            SELECT 1 FROM Orders o
            WHERE o.user.id = :userId
              AND o.packages.packageId = k.aPackage.packageId
              AND o.status = com.sbaproject.sbamindmap.enums.OrderStatus.COMPLETED
         )
    """)
    List<ApiKey> findAvailableKeys(@Param("userId") Long userId, @Param("now") Instant now);
}
