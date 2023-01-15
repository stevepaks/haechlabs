package com.example.foo1.domain.wallet.dto;

import com.example.foo1.domain.wallet.entity.Wallet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletDto {

    private Long walletId;

    private String password;

    private String name;

    private String address;

    @Builder
    private WalletDto(Long walletId, String password, String name, String address) {
        this.walletId = walletId;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    public static WalletDto getWalletDto(Wallet wallet) {
        return WalletDto.builder()
                .walletId(wallet.getId())
                .address(wallet.getAddress())
                .name(wallet.getName())
                .password(wallet.getPassword())
                .build();
    }
}
