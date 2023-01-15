package com.example.foo1.domain.account.repository;

import com.example.foo1.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByWalletId(Long walletId);
}
