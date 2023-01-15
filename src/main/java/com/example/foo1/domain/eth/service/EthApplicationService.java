package com.example.foo1.domain.eth.service;

import com.example.foo1.domain.wallet.TransactionState;
import com.example.foo1.domain.wallet.TransactionStatus;
import com.example.foo1.domain.wallet.service.WalletService;
import com.example.foo1.messaging.dto.AppendAccountTransactionEventDto;
import com.example.foo1.messaging.dto.TransactionEventDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.Transaction;

@Service
public class EthApplicationService {

    @Value("${cloud.aws.sqs.update-account-balance-queue.name}")
    private String updateAccountBalanceQueueName;

    @Value("${cloud.aws.sqs.append-account-transaction-queue.name}")
    private String appendAccountTransactionQueueName;

    private final EthService ethService;

    private final QueueMessagingTemplate queueMessagingTemplate;

    private final WalletService walletService;

    public EthApplicationService(EthService ethService, QueueMessagingTemplate queueMessagingTemplate, WalletService walletService) {
        this.ethService = ethService;
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.walletService = walletService;
        subscribeTransactions();
    }

    private void subscribeTransactions() {
        ethService.getTransactionFlowable().subscribe(transaction -> {
            if (transaction != null && transaction.getType() != null) {
                sendEvent(transaction, transaction.getTo(), TransactionState.DEPOSIT.name());
                sendEvent(transaction, transaction.getFrom(), TransactionState.WITHDRAW.name());
            }
        });
    }

    private void sendEvent(Transaction transaction, String walletName, String transactionState) {
        if (walletName != null && walletService.existWallet(walletName)) {
            if (transaction.getType().equals(TransactionStatus.CONFIRMED.getStatus())) {
                queueMessagingTemplate.convertAndSend(updateAccountBalanceQueueName, TransactionEventDto.of(transaction, transactionState));
            } else {
                queueMessagingTemplate.convertAndSend(appendAccountTransactionQueueName, AppendAccountTransactionEventDto.of(transaction, transactionState));
            }
        }
    }

}
