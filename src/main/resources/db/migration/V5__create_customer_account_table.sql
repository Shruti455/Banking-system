CREATE TABLE customer_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    crn VARCHAR(50) NOT NULL,
    acc_number VARCHAR(50) NOT NULL,
    acc_status VARCHAR(20) NOT NULL DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_customer_account_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_customer_account_crn UNIQUE (crn),
    CONSTRAINT uk_customer_account_number UNIQUE (acc_number)
);