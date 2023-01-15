package com.example.foo1.domain.wallet.service;

import com.example.foo1.domain.eth.dto.WalletCredentialDto;
import com.example.foo1.domain.wallet.dto.WalletDto;
import com.example.foo1.domain.wallet.entity.Wallet;
import com.example.foo1.domain.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional(readOnly = true)
    public WalletDto getById(Long walletId) {
        return walletRepository.findById(walletId)
                .map(WalletDto::getWalletDto)
                .orElseThrow(() -> new IllegalArgumentException(String.format("not found wallet. walletId: %s", walletId)));
    }

    @Transactional
    public Long createWallet(WalletCredentialDto walletCredentials, String password) {

        return walletRepository.save(
                Wallet.builder()
                        .name(walletCredentials.getWalletName())
                        .password(password)
                        .address(walletCredentials.getCredentials().getAddress())
                        .build()
        ).getId();
    }

    @Transactional(readOnly = true)
    public WalletDto getByAddress(String walletAddress) {

        return walletRepository.findByAddress(walletAddress)
                .map(WalletDto::getWalletDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existWallet(String walletAddress) {

        return walletRepository.findByAddress(walletAddress).isPresent();
    }

}
