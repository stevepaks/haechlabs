package com.example.foo1.domain.account.web.request;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferAmountRequest {

    @NotNull
    private String toWalletAddress;

    @NotNull
    private Double amount;

}
