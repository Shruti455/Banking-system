CREATE TABLE customer_auth (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    customer_id BIGINT NOT NULL UNIQUE,

    password_hash VARCHAR(255) NOT NULL,

    failed_attempts INT DEFAULT 0,

    account_locked BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_customer_auth_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
);