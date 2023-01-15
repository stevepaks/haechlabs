package com.example.foo1.domain.account.service;

import com.example.foo1.domain.account.dto.AccountDto;
import com.example.foo1.domain.account.entity.Account;
import com.example.foo1.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void update(Long walletId, BigDecimal balance) {
        final Account currentAccount = accountRepository.findByWalletId(walletId)
                .orElseThrow(RuntimeException::new);
        currentAccount.updateBalance(balance.doubleValue());
    }

    @Transactional(readOnly = true)
    public AccountDto getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .map(AccountDto::of)
                .orElseThrow(() -> new IllegalArgumentException(String.format("not found account. accountId: %s", accountId)));
    }

    @Transactional
    public void createAccount(AccountDto accountDto) {
        accountRepository.save(
                Account.builder()
                        .walletId(accountDto.getWalletId())
                        .balance(0.0)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build()
        );
    }

    @Transactional
    public AccountDto getAccountByWalletId(Long walletId) {
        return accountRepository.findByWalletId(walletId)
                .map(AccountDto::of)
                .orElseThrow(() -> new IllegalArgumentException(String.format("not fount account. walletId: %s", walletId)));
    }
}
