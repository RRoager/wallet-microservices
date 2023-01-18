package com.rroager.walletservice.repository;

import com.rroager.walletservice.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findById(UUID id);
}
