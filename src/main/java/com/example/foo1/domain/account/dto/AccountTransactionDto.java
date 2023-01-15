package com.example.foo1.domain.account.dto;

import com.example.foo1.domain.account.entity.AccountTransaction;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountTransactionDto {

    private Long accountId;
    private String transactionHash;
    private String transactionState;
    private String transactionStatus;
    private Integer blockConfirmationCount;
    private Double amount;

    @Builder
    private AccountTransactionDto(
            Long accountId,
            String transactionHash,
            String transactionState,
            String transactionStatus,
            Integer blockConfirmationCount,
            Double amount) {
        this.accountId = accountId;
        this.transactionHash = transactionHash;
        this.transactionState = transactionState;
        this.transactionStatus = transactionStatus;
        this.blockConfirmationCount = blockConfirmationCount;
        this.amount = amount;
    }

    public static AccountTransactionDto of(Long accountId, Transaction transaction, String transactionState, BigInteger blockConfirmationCount) {
        return AccountTransactionDto.builder()
                .transactionHash(transaction.getHash())
                .accountId(accountId)
                .transactionState(transactionState)
                .transactionStatus(transaction.getType())
                .blockConfirmationCount(blockConfirmationCount.intValue())
                .amount(transaction.getValue().doubleValue())
                .build();
    }

    public static AccountTransactionDto of(AccountTransaction accountTransaction) {
        return AccountTransactionDto.builder()
                .accountId(accountTransaction.getAccountId())
                .transactionState(accountTransaction.getState())
                .transactionStatus(accountTransaction.getStatus())
                .transactionHash(accountTransaction.getHash())
                .blockConfirmationCount(accountTransaction.getBlockConfirmationCount())
                .amount(accountTransaction.getAmount())
                .build();
    }
}
