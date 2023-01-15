package com.example.foo1.domain.wallet.repository;

import com.example.foo1.domain.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByAddress(String address);
}