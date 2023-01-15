package com.example.foo1.domain.account.service;

import com.example.foo1.domain.account.dto.AccountTransactionDto;
import com.example.foo1.domain.account.dto.GetAccountTransactionRequest;
import com.example.foo1.domain.account.entity.AccountTransaction;
import com.example.foo1.domain.account.repository.AccountTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountTransactionService {

    private final AccountTransactionRepository accountTransactionRepository;

    public AccountTransactionService(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    @Transactional
    public void save(AccountTransactionDto accountTransactionDto) {

        accountTransactionRepository.save(AccountTransaction.of(accountTransactionDto));
    }

    @Transactional
    public List<AccountTransactionDto> getAccountTransactions(Long accountId, GetAccountTransactionRequest getAccountTransactionRequest) {
        return accountTransactionRepository.findAccountTransactionsMore(accountId, getAccountTransactionRequest).stream()
                .map(AccountTransactionDto::of)
                .collect(Collectors.toList());
    }

}
