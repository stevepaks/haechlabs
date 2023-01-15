package com.example.foo1.domain.account.repository;

import com.example.foo1.domain.account.entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long>, AccountTransactionRepositoryCustom {
}
