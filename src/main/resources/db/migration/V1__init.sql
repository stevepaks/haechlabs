CREATE TABLE IF NOT EXISTS account
(
    id          BIGINT   PRIMARY KEY AUTO_INCREMENT,
    wallet_id   BIGINT   NOT NULL,
    balance     DOUBLE  NOT NULL,
    created_at  DATETIME NOT NULL,
    modified_at DATETIME NOT NULL
) ENGINE 'InnoDB';

CREATE INDEX IDX_wallet_id ON account (wallet_id);

CREATE TABLE IF NOT EXISTS wallet
(
    id BIGINT PRIMARY KEY  AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL ,
    password VARCHAR(100) NOT NULL ,
    address VARCHAR(100) NOT NULL
) ENGINE  'InnoDB';

CREATE INDEX IDX_address ON wallet (name);

CREATE TABLE IF NOT EXISTS account_transaction
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL ,
    hash VARCHAR(100) NOT NULL ,
    state VARCHAR(20) NOT NULL ,
    status VARCHAR(20) NOT NULL ,
    block_confirmation_count INT NOT NULL ,
    amount DOUBLE not null ,
    created_at DATETIME NOT NULL
) ENGINE 'InnoDB';

CREATE INDEX IDX_created_at ON account_transaction(created_at);

CREATE INDEX IDX_account_id ON account_transaction(account_id);

