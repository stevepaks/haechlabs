package com.example.foo1.messaging.listener;

import com.example.foo1.domain.account.app.AccountApplicationService;
import com.example.foo1.domain.eth.service.EthService;
import com.example.foo1.domain.wallet.TransactionState;
import com.example.foo1.domain.wallet.dto.WalletDto;
import com.example.foo1.domain.wallet.service.WalletService;
import com.example.foo1.messaging.dto.AppendAccountTransactionEventDto;
import com.example.foo1.messaging.dto.PendingTransactionEventDto;
import com.example.foo1.messaging.dto.TransactionEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Component
public class TransactionEventListener {

    @Value("${cloud.aws.sqs.update-account-balance-queue.url}")
    private String updateAccountBalanceQueueName;

    private final WalletService walletService;

    private final EthService ethService;

    private final AccountApplicationService accountApplicationService;

    private final QueueMessagingTemplate queueMessagingTemplate;

    public TransactionEventListener(WalletService walletService, EthService ethService,
                                    AccountApplicationService accountApplicationService, QueueMessagingTemplate queueMessagingTemplate) {
        this.walletService = walletService;
        this.ethService = ethService;
        this.accountApplicationService = accountApplicationService;
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    @SqsListener(value = "${cloud.aws.sqs.pending-transaction-queue.url}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    private void processPendingTransaction(PendingTransactionEventDto pendingTransactionEventDto) throws Exception {

        final Long fromWalletId = pendingTransactionEventDto.getWalletId();
        final String toWalletAddress = pendingTransactionEventDto.getToWalletAddress();
        final Double amount = pendingTransactionEventDto.getAmount();
        log.info("[TransactionEventListener] received pendingTransactionEventDto. fromWalletId: {}, toWalletAddress: {}, amount: {}", fromWalletId, toWalletAddress, amount);

        final WalletDto fromWallet = walletService.getById(fromWalletId);

        if (fromWallet == null) {
            return;
        }

        String transactionHash;
        // TODO SqsMessageDeletionPolicy.NEVER??? ?????? ???
        log.info("[TransactionEventListener] transfer amount. fromWalletAddress: {}, toWalletAddress: {}, amount: {}", fromWallet.getAddress(), toWalletAddress, amount);
        try {
            transactionHash = ethService.transferTo(fromWallet, toWalletAddress, amount);

            // TODO ????????? ack ?????? ????????? ??????
        } catch (Exception e) {

            // TODO 3??? ?????? ?????? ?????? ?????? Dead queue??? ?????? ??? ?????? ?????? ??? ????????? ??????????????? ??????
            log.error("[TransactionEventListener] failed to transfer amount. fromWallet: {}, toWallerAddress: {}, amount: {}", fromWallet, toWalletAddress, amount);
            transactionHash = ethService.transferTo(fromWallet, toWalletAddress, amount);
        }

        Transaction transaction = ethService.getTransaction(transactionHash);
        queueMessagingTemplate.convertAndSend(updateAccountBalanceQueueName, TransactionEventDto.of(transaction, TransactionState.WITHDRAW.name()));
    }

    @SqsListener(value = "${cloud.aws.sqs.update-account-balance-queue.url}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    private void updateAccountBalance(TransactionEventDto transactionEventDto) throws IOException {

        final String transactionHash = transactionEventDto.getTransactionHash();
        final String fromWalletAddress = transactionEventDto.getFromWalletAddress();
        final String toWalletAddress = transactionEventDto.getToWalletAddress();
        final String transactionState = transactionEventDto.getTransactionState();
        final String transactionStatus = transactionEventDto.getTransactionStatus();
        final BigDecimal fromWalletBalance = ethService.getBalance(fromWalletAddress);
        final BigDecimal toWalletBalance = ethService.getBalance(toWalletAddress);
        log.info(
                "[TransactionEventListener] received transactionEventDto. transactionHash: {}, fromWalletAddress: {}, fromWalletBalance: {}, toWalletAddress: {}, toWalletBalance: {}, state: {}, status: {}",
                transactionHash,
                fromWalletAddress,
                fromWalletBalance,
                toWalletAddress,
                toWalletBalance,
                transactionState,
                transactionStatus
        );

        // ??????
        accountApplicationService.updateAccount(transactionHash, transactionState, transactionStatus, fromWalletAddress, fromWalletBalance);

        // ??????
        accountApplicationService.updateAccount(transactionHash, transactionState, transactionStatus, toWalletAddress, toWalletBalance);
    }

    @SqsListener(value = "${cloud.aws.sqs.append-account-transaction-queue.url}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    private void appendAccountTransaction(AppendAccountTransactionEventDto appendAccountTransactionEventDto) throws IOException {
        final String transactionHash = appendAccountTransactionEventDto.getTransactionHash();
        final String fromWalletAddress = appendAccountTransactionEventDto.getFromWalletAddress();
        final String toWalletAddress = appendAccountTransactionEventDto.getToWalletAddress();
        final String transactionState = appendAccountTransactionEventDto.getTransactionState();
        log.info(
                "[TransactionEventListener] received appendAccountTransactionEventDto. transactionHash: {}, fromWalletAddress: {}, toWalletAddress: {}, transactionState: {}",
                transactionHash,
                fromWalletAddress,
                toWalletAddress,
                transactionState
        );

        // ??????
        accountApplicationService.appendAccountTransaction(transactionHash, transactionState, fromWalletAddress);

        // ??????
        accountApplicationService.appendAccountTransaction(transactionHash, transactionState, toWalletAddress);
    }

}
