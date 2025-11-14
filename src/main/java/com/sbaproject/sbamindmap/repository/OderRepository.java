package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser_Id(Long userId);
    List<Orders> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Orders o WHERE o.status = 'ACTIVE' AND o.expiredAt < :now")
    List<Orders> findExpiredActiveOrders(LocalDateTime now);
}
