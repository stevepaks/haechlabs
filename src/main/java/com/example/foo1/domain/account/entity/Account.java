package com.example.foo1.domain.account.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_id")
    private Long walletId;

    private Double balance = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Builder
    private Account(Long walletId, Double balance, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.walletId = walletId;
        this.balance = balance;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public void updateBalance(double balance) {
        this.balance = balance;
        this.modifiedAt = LocalDateTime.now();
    }
}
