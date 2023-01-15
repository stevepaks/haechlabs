package com.example.foo1.messaging.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PendingTransactionEventDto {

    private Long walletId;

    private String toWalletAddress;

    private Double amount;

    @Builder
    private PendingTransactionEventDto(Long walletId, String toWalletAddress, Double amount) {
        this.walletId = walletId;
        this.toWalletAddress = toWalletAddress;
        this.amount = amount;
    }
}
