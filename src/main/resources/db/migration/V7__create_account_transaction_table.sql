CREATE TABLE account_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    trans_id VARCHAR(100) NOT NULL,
    trans_method VARCHAR(50) NOT NULL,
    trans_amount DECIMAL(15,2) NOT NULL,
    trans_message VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_transaction_account
        FOREIGN KEY (account_id)
        REFERENCES customer_account(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_account_transaction_trans_id UNIQUE (trans_id)
);