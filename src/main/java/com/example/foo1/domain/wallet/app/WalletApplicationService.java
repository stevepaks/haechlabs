package com.example.foo1.domain.wallet.app;

import com.example.foo1.domain.account.dto.AccountDto;
import com.example.foo1.domain.account.service.AccountService;
import com.example.foo1.domain.eth.dto.WalletCredentialDto;
import com.example.foo1.domain.eth.service.EthService;
import com.example.foo1.domain.wallet.service.WalletService;
import com.example.foo1.domain.wallet.web.request.CreateWalletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class WalletApplicationService {

    private final WalletService walletService;

    private final EthService ethService;

    private final AccountService accountService;

    public WalletApplicationService(WalletService walletService, EthService ethService, AccountService accountService) {
        this.walletService = walletService;
        this.ethService = ethService;
        this.accountService = accountService;
    }

    @Transactional
    public void createWallet(CreateWalletRequest createWalletRequest)
            throws InvalidAlgorithmParameterException, CipherException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
        final WalletCredentialDto walletCredentials = ethService.generateWalletCredentials(createWalletRequest.getWalletPassword());
        final Long walletId = walletService.createWallet(walletCredentials, createWalletRequest.getWalletPassword());
        accountService.createAccount(AccountDto.of(walletId));
    }


}
