package com.example.foo1.domain.account.dto;

import com.example.foo1.domain.account.entity.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountDto {

    private Long accountId;
    private Long walletId;
    private Double balance = 0.0;

    @Builder
    private AccountDto(Long accountId, Long walletId, Double balance) {
        this.accountId = accountId;
        this.walletId = walletId;
        this.balance = balance;
    }

    public static AccountDto of(Account account) {
        return AccountDto.builder()
                .accountId(account.getId())
                .walletId(account.getWalletId())
                .balance(account.getBalance())
                .build();
    }

    public static AccountDto of(Long walletId) {
        return AccountDto.builder()
                .walletId(walletId)
                .build();
    }
}
