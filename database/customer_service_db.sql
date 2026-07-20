-- =========================================================================
-- customer_service_db
-- Corresponds to hibernate.ddl-auto=update entities in customer-service.
-- Provided for reference / manual provisioning / DBA review.
-- =========================================================================

CREATE DATABASE IF NOT EXISTS customer_service_db;
USE customer_service_db;

CREATE TABLE corporate_customers (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name           VARCHAR(150)  NOT NULL,
    cif_number             VARCHAR(20)   NOT NULL,
    registration_number    VARCHAR(50)   NOT NULL,
    email                  VARCHAR(100)  NOT NULL,
    phone_number           VARCHAR(20),
    address                VARCHAR(255),
    status                 VARCHAR(20)   NOT NULL,
    created_at             DATETIME      NOT NULL,
    updated_at             DATETIME,
    created_by             VARCHAR(100),
    updated_by             VARCHAR(100),
    CONSTRAINT uk_cif_number UNIQUE (cif_number),
    CONSTRAINT uk_registration_number UNIQUE (registration_number),
    CONSTRAINT chk_customer_status CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED','CLOSED'))
) ENGINE=InnoDB;

CREATE TABLE corporate_users (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id    BIGINT        NOT NULL,
    username       VARCHAR(50)   NOT NULL,
    email          VARCHAR(100)  NOT NULL,
    full_name      VARCHAR(100)  NOT NULL,
    role           VARCHAR(20)   NOT NULL,
    status         VARCHAR(20)   NOT NULL,
    created_at     DATETIME      NOT NULL,
    updated_at     DATETIME,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    CONSTRAINT uk_username UNIQUE (username),
    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT fk_user_customer FOREIGN KEY (customer_id) REFERENCES corporate_customers(id),
    CONSTRAINT chk_user_role CHECK (role IN ('MAKER','CHECKER','ADMIN')),
    CONSTRAINT chk_user_status CHECK (status IN ('ACTIVE','INACTIVE','LOCKED'))
) ENGINE=InnoDB;

CREATE INDEX idx_users_customer_id ON corporate_users(customer_id);
CREATE INDEX idx_users_role ON corporate_users(role);
