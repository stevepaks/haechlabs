package com.example.foo1.domain.account.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccountTransaction is a Querydsl query type for AccountTransaction
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAccountTransaction extends EntityPathBase<AccountTransaction> {

    private static final long serialVersionUID = 800807811L;

    public static final QAccountTransaction accountTransaction = new QAccountTransaction("accountTransaction");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final NumberPath<Integer> blockConfirmationCount = createNumber("blockConfirmationCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath hash = createString("hash");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath state = createString("state");

    public final StringPath status = createString("status");

    public QAccountTransaction(String variable) {
        super(AccountTransaction.class, forVariable(variable));
    }

    public QAccountTransaction(Path<? extends AccountTransaction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountTransaction(PathMetadata metadata) {
        super(AccountTransaction.class, metadata);
    }

}

