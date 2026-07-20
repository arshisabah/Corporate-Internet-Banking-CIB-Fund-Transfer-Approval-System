-- =========================================================================
-- beneficiary_service_db
-- customer_id is a plain reference to customer_service_db.corporate_customers.id
-- - NOT a foreign key, since these are separate databases (database-per-service).
-- =========================================================================

CREATE DATABASE IF NOT EXISTS beneficiary_service_db;
USE beneficiary_service_db;

CREATE TABLE beneficiaries (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id       BIGINT        NOT NULL,
    beneficiary_name  VARCHAR(150)  NOT NULL,
    account_number    VARCHAR(34)   NOT NULL,
    bank_name         VARCHAR(150)  NOT NULL,
    ifsc_code         VARCHAR(15)   NOT NULL,
    status            VARCHAR(20)   NOT NULL,
    created_at        DATETIME      NOT NULL,
    updated_at        DATETIME,
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100),
    CONSTRAINT chk_beneficiary_status CHECK (status IN ('ACTIVE','INACTIVE','BLOCKED'))
) ENGINE=InnoDB;

CREATE INDEX idx_beneficiary_customer_id ON beneficiaries(customer_id);
CREATE UNIQUE INDEX uk_beneficiary_customer_account_bank
    ON beneficiaries(customer_id, account_number, bank_name);
