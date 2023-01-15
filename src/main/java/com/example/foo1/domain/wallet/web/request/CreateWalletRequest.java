package com.example.foo1.domain.wallet.web.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateWalletRequest {

    private String walletPassword;

}
