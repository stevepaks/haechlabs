package com.example.foo1.domain.wallet.web;

import com.example.foo1.domain.wallet.app.WalletApplicationService;
import com.example.foo1.domain.wallet.web.request.CreateWalletRequest;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletApplicationService walletApplicationService;

    public WalletController(WalletApplicationService walletApplicationService) {
        this.walletApplicationService = walletApplicationService;
    }

    @PostMapping
    public void createWallet(@RequestBody CreateWalletRequest createWalletRequest)
            throws InvalidAlgorithmParameterException, CipherException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
        walletApplicationService.createWallet(createWalletRequest);
    }

}
