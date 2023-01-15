package com.example.foo1.domain.account.repository;

import com.example.foo1.domain.account.dto.GetAccountTransactionRequest;
import com.example.foo1.domain.account.entity.AccountTransaction;
import java.util.List;

public interface AccountTransactionRepositoryCustom {

    List<AccountTransaction> findAccountTransactionsMore(Long accountId, GetAccountTransactionRequest getAccountTransactionRequest);
}
