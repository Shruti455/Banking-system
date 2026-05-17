CREATE TABLE branch_settings (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    branch_name VARCHAR(150) NOT NULL,

    branch_code VARCHAR(50) NOT NULL UNIQUE,

    ifsc_code VARCHAR(20) NOT NULL UNIQUE,

    branch_address TEXT,

    city VARCHAR(100),

    state VARCHAR(100),

    country VARCHAR(100) DEFAULT 'India',

    pincode VARCHAR(10),

    contact_number VARCHAR(20),

    email VARCHAR(150),

    status TINYINT(1) DEFAULT 1,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO branch_settings (

    branch_name,
    branch_code,
    ifsc_code,
    branch_address,
    city,
    state,
    country,
    pincode,
    contact_number,
    email,
    status

) VALUES (

    'Main Branch',
    'BR001',
    'IFSC000001',
    'Head Office Address',
    'Bihar',
    'Patna',
    'India',
    '600001',
    '9876543210',
    'mainbranch@bank.com',
    1
);