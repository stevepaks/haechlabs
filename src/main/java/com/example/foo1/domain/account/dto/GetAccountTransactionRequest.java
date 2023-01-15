package com.example.foo1.domain.account.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAccountTransactionRequest {

    private Boolean isConfirmed;
    private Long offset;
    private Long limit = 10L;
    private LocalDateTime createdAt;

    @Builder
    private GetAccountTransactionRequest(Boolean isConfirmed, Long offset, Long limit, LocalDateTime createdAt) {
        this.isConfirmed = isConfirmed;
        this.offset = offset;
        this.limit = limit;
        this.createdAt = createdAt;
    }
}
