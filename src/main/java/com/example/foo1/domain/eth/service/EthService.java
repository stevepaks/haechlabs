package com.example.foo1.domain.eth.service;

import com.example.foo1.domain.eth.dto.WalletCredentialDto;
import com.example.foo1.domain.wallet.dto.WalletDto;
import io.reactivex.Flowable;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class EthService {

    private final String WALLET_DIRECTORY = "./";

    private final Web3j web3;

    public EthService(Web3j web3) {
        this.web3 = web3;
    }

    public WalletCredentialDto generateWalletCredentials(String walletPassword) throws InvalidAlgorithmParameterException, CipherException, NoSuchAlgorithmException, IOException, NoSuchProviderException {

        String walletName = WalletUtils.generateNewWalletFile(walletPassword, new File(WALLET_DIRECTORY));
        final Credentials credentials = WalletUtils.loadCredentials(walletPassword, WALLET_DIRECTORY + walletName);
        return WalletCredentialDto.builder()
                .walletName(walletName)
                .credentials(credentials)
                .build();
    }

    public BigDecimal getBalance(String walletAddress) throws IOException {

        return Convert.fromWei(
                web3.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST)
                        .send()
                        .getBalance()
                        .toString(),
                Convert.Unit.ETHER
        );
    }

    public String transferTo(WalletDto walletDto, String toWalletAddress, Double amount) throws Exception {

        return Transfer.sendFunds(
                        web3,
                        getCredentials(walletDto),
                        toWalletAddress,
                        BigDecimal.valueOf(amount),
                        Convert.Unit.ETHER
                ).send()
                .getTransactionHash();
    }

    private Credentials getCredentials(WalletDto walletDto) throws IOException, CipherException {
        return WalletUtils.loadCredentials(walletDto.getPassword(), String.format("%s/%s", WALLET_DIRECTORY, walletDto.getName()));
    }

    public Flowable<Transaction> getTransactionFlowable() {
        return web3.transactionFlowable();
    }

    public BigInteger getBlockConfirmationCount() throws IOException {
        return web3.ethBlockNumber().send().getBlockNumber();
    }

    public Transaction getTransaction(String transactionHash) throws IOException {
        return web3.ethGetTransactionByHash(transactionHash).send().getTransaction().orElse(null);
    }
}
