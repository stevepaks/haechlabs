package com.example.foo1.domain.wallet;

import lombok.Getter;

@Getter
public enum TransactionStatus {

    PENDING("0x0"),
    MINED("0x1"),
    CONFIRMED("0x2");

    private final String status;

    TransactionStatus(String status) {
        this.status = status;
    }
}
