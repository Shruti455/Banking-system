CREATE TABLE account_balance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    curr_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    hold_balance DECIMAL(15,2) DEFAULT NULL,
    hold_reason VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_balance_account
        FOREIGN KEY (account_id)
        REFERENCES customer_account(id)
        ON DELETE CASCADE
);