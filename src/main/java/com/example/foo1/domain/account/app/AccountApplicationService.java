package com.example.foo1.domain.account.app;

import com.example.foo1.domain.account.dto.AccountDto;
import com.example.foo1.domain.account.dto.AccountTransactionDto;
import com.example.foo1.domain.account.dto.GetAccountTransactionRequest;
import com.example.foo1.domain.account.service.AccountService;
import com.example.foo1.domain.account.service.AccountTransactionService;
import com.example.foo1.domain.account.web.request.TransferAmountRequest;
import com.example.foo1.domain.eth.service.EthService;
import com.example.foo1.domain.wallet.dto.WalletDto;
import com.example.foo1.domain.wallet.service.WalletService;
import com.example.foo1.messaging.dto.PendingTransactionEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
public class AccountApplicationService {

    @Value("${cloud.aws.sqs.pending-transaction-queue.name}")
    private String pendingTransactionQueueName;
    private final AccountService accountService;
    private final WalletService walletService;
    private final AccountTransactionService accountTransactionService;
    private final EthService ethService;
    private final QueueMessagingTemplate queueMessagingTemplate;

    public AccountApplicationService(
            AccountService accountService,
            WalletService walletService,
            AccountTransactionService accountTransactionService,
            EthService ethService,
            QueueMessagingTemplate queueMessagingTemplate) {
        this.accountService = accountService;
        this.walletService = walletService;
        this.accountTransactionService = accountTransactionService;
        this.ethService = ethService;
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    @Transactional(readOnly = true)
    public Double getBalance(Long accountId) {
        return accountService.getAccount(accountId)
                .getBalance();
    }

    @Transactional
    public void transferTo(Long accountId, TransferAmountRequest transferAmountRequest) {

        final Double amount = transferAmountRequest.getAmount();
        final AccountDto accountDto = accountService.getAccount(accountId);
        checkInsufficientBalance(accountDto, amount);

        final WalletDto walletDto = walletService.getById(accountDto.getWalletId());
        queueMessagingTemplate.convertAndSend(
                pendingTransactionQueueName,
                PendingTransactionEventDto.builder()
                        .walletId(walletDto.getWalletId())
                        .toWalletAddress(transferAmountRequest.getToWalletAddress())
                        .amount(amount)
                        .build()
        );
    }

    private boolean checkInsufficientBalance(AccountDto accountDto, Double amount) {

        final Double accountBalance = accountDto.getBalance();
        if (accountBalance.compareTo(amount) < 0) {
            final Long accountId = accountDto.getAccountId();
            throw new IllegalArgumentException(String.format("not enough balance for transferring. accountId: %s, accountBalance: %s, amount: %s", accountId, accountBalance, amount));
        }

        // TODO pending을 제외한 나머지 트랜젝션 + GAS 를 고려하여 체크
        return false;
    }

    @Transactional
    public void updateAccount(String transactionHash, String transactionState, String transactionStatus, String walletAddress, BigDecimal balance) throws IOException {

        final WalletDto walletDto = walletService.getByAddress(walletAddress);
        if (walletDto == null) {
            return;
        }

        log.info("[AccountApplicationService] update account. transactionHash: {}, walletAddress: {}, transactionStatus: {}, balance: {}", transactionHash, walletAddress, transactionStatus, balance.doubleValue());

        final Long walletId = walletDto.getWalletId();
        accountService.update(walletId, balance);

        final AccountDto updatedAccountDto = accountService.getAccountByWalletId(walletId);

        final Transaction transaction = ethService.getTransaction(transactionHash);

        final BigInteger blockConfirmationCount = ethService.getBlockConfirmationCount();

        // TODO crateAt을 transaction 정보를 참조하여 저장
        accountTransactionService.save(AccountTransactionDto.of(updatedAccountDto.getAccountId(), transaction, transactionState, blockConfirmationCount));
    }

    @Transactional
    public void appendAccountTransaction(String transactionHash, String transactionState, String walletAddress) throws IOException {

        final WalletDto walletDto = walletService.getByAddress(walletAddress);
        if (walletDto == null) {
            return;
        }

        log.info("[AccountApplicationService] append account transaction. transactionHash: {}, walletAddress: {}, transactionState: {}", transactionHash, walletAddress, transactionState);

        final AccountDto accountDto = accountService.getAccountByWalletId(walletDto.getWalletId());

        final Transaction transaction = ethService.getTransaction(transactionHash);

        final BigInteger blockConfirmationCount = ethService.getBlockConfirmationCount();

        accountTransactionService.save(AccountTransactionDto.of(accountDto.getAccountId(), transaction, transactionState, blockConfirmationCount));
    }

    @Transactional(readOnly = true)
    public List<AccountTransactionDto> getAccountTransactionsMore(Long accountId, GetAccountTransactionRequest getAccountTransactionRequest) {

        return accountTransactionService.getAccountTransactions(accountId, getAccountTransactionRequest);
    }
}
