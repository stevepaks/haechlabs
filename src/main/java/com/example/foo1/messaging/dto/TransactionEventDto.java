package com.example.foo1.messaging.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionEventDto {

    private String transactionHash;
    private String fromWalletAddress;
    private String toWalletAddress;
    private BigInteger transactionValue;
    // 입금, 출금 구분
    private String transactionState;
    // Pending, Mined, Confirmed 구분
    private String transactionStatus;

    @Builder
    private TransactionEventDto(String transactionHash, String fromWalletAddress, String toWalletAddress,
                                String transactionState, BigInteger transactionValue, String transactionStatus) {
        this.transactionHash = transactionHash;
        this.fromWalletAddress = fromWalletAddress;
        this.toWalletAddress = toWalletAddress;
        this.transactionValue = transactionValue;
        this.transactionState = transactionState;
        this.transactionStatus = transactionStatus;
    }

    public static TransactionEventDto of(Transaction transaction, String transactionState) {
        return TransactionEventDto.builder()
                .transactionHash(transaction.getHash())
                .fromWalletAddress(transaction.getFrom())
                .toWalletAddress(transaction.getTo())
                .transactionValue(transaction.getValue())
                .transactionState(transactionState)
                .transactionStatus(transaction.getType())
                .build();
    }
}
