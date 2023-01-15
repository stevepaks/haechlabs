package com.example.foo1.messaging.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.web3j.protocol.core.methods.response.Transaction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppendAccountTransactionEventDto {

    private String transactionHash;
    private String fromWalletAddress;
    private String toWalletAddress;
    private String transactionState;

    @Builder
    private AppendAccountTransactionEventDto(String transactionHash, String fromWalletAddress, String toWalletAddress, String transactionState) {
        this.transactionHash = transactionHash;
        this.fromWalletAddress = fromWalletAddress;
        this.toWalletAddress = toWalletAddress;
        this.transactionState = transactionState;
    }

    public static AppendAccountTransactionEventDto of(Transaction transaction, String transactionState) {
        return AppendAccountTransactionEventDto.builder()
                .transactionHash(transaction.getHash())
                .fromWalletAddress(transaction.getFrom())
                .toWalletAddress(transaction.getTo())
                .transactionState(transactionState)
                .build();
    }
}
