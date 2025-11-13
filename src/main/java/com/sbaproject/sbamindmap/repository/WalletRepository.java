package com.sbaproject.sbamindmap.repository;

import com.sbaproject.sbamindmap.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser_Id(Long userId);

    Optional<Wallet> findByUserId(Long userId);
}
