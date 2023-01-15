package com.example.foo1.domain.eth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.web3j.crypto.Credentials;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletCredentialDto {
    private String walletName;
    private Credentials credentials;

    @Builder
    private WalletCredentialDto(String walletName, Credentials credentials) {
        this.walletName = walletName;
        this.credentials = credentials;
    }
}
