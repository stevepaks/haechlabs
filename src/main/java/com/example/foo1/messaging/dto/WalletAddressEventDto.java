package com.example.foo1.messaging.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletAddressEventDto {

    private String walletAddress;

    @Builder
    private WalletAddressEventDto(String walletAddress) {
        this.walletAddress = walletAddress;
    }
}
