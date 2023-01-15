package com.example.foo1.domain.account.entity;

import com.example.foo1.domain.account.dto.AccountTransactionDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "account_transaction")
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    private String hash;

    private String state;

    private String status;

    @Column(name = "block_confirmation_count")
    private Integer blockConfirmationCount;

    private Double amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    private AccountTransaction(Long accountId, String hash, String state, String status,
                               Integer blockConfirmationCount, Double amount, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.hash = hash;
        this.state = state;
        this.status = status;
        this.blockConfirmationCount = blockConfirmationCount;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static AccountTransaction of(AccountTransactionDto accountTransactionDto) {
        return AccountTransaction.builder()
                .hash(accountTransactionDto.getTransactionHash())
                .accountId(accountTransactionDto.getAccountId())
                .state(accountTransactionDto.getTransactionState())
                .status(accountTransactionDto.getTransactionStatus())
                .blockConfirmationCount(accountTransactionDto.getBlockConfirmationCount())
                .amount(accountTransactionDto.getAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
