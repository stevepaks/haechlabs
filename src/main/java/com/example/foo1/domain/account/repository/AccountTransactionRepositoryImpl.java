package com.example.foo1.domain.account.repository;

import com.example.foo1.domain.account.dto.GetAccountTransactionRequest;
import com.example.foo1.domain.account.entity.AccountTransaction;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.example.foo1.domain.account.entity.QAccountTransaction.accountTransaction;

public class AccountTransactionRepositoryImpl extends QuerydslRepositorySupport implements AccountTransactionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public AccountTransactionRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(AccountTransaction.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<AccountTransaction> findAccountTransactionsMore(Long accountId, GetAccountTransactionRequest getAccountTransactionRequest) {

        final BooleanBuilder booleanBuilder = new BooleanBuilder(accountTransaction.accountId.eq(accountId));
        if (getAccountTransactionRequest.getOffset() != null) {
            booleanBuilder.and(accountTransaction.id.lt(getAccountTransactionRequest.getOffset()));
        }

        if (getAccountTransactionRequest.getIsConfirmed() != null && getAccountTransactionRequest.getIsConfirmed()) {
            booleanBuilder.and(accountTransaction.status.eq("0x2"));
        }

        return from(accountTransaction)
                .where(booleanBuilder)
                .orderBy(accountTransaction.id.desc())
                .limit(Optional.ofNullable(getAccountTransactionRequest.getLimit()).orElse(10L))
                .fetch();
    }
}
