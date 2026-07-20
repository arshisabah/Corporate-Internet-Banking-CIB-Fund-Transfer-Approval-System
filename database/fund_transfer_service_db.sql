-- =========================================================================
-- fund_transfer_service_db
-- customer_id and beneficiary_id are plain references into other services'
-- databases - no cross-DB foreign keys (database-per-service pattern).
-- =========================================================================

CREATE DATABASE IF NOT EXISTS fund_transfer_service_db;
USE fund_transfer_service_db;

CREATE TABLE fund_transfers (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    transfer_ref_no   VARCHAR(40)    NOT NULL,
    customer_id       BIGINT         NOT NULL,
    beneficiary_id    BIGINT         NOT NULL,
    amount            DECIMAL(19,2)  NOT NULL,
    currency          VARCHAR(3)     NOT NULL,
    remarks           VARCHAR(500),
    status            VARCHAR(20)    NOT NULL,
    created_at        DATETIME       NOT NULL,
    updated_at        DATETIME,
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100),
    CONSTRAINT uk_transfer_ref_no UNIQUE (transfer_ref_no),
    CONSTRAINT chk_transfer_status CHECK (status IN
        ('INITIATED','VALIDATED','PENDING_APPROVAL','APPROVED','REJECTED','SENT_BACK','EXECUTED','FAILED')),
    CONSTRAINT chk_transfer_amount CHECK (amount > 0)
) ENGINE=InnoDB;

CREATE TABLE transfer_status_history (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    transfer_id   BIGINT        NOT NULL,
    old_status    VARCHAR(20),
    new_status    VARCHAR(20)   NOT NULL,
    changed_at    DATETIME      NOT NULL,
    changed_by    VARCHAR(100),
    remarks       VARCHAR(500),
    CONSTRAINT fk_history_transfer FOREIGN KEY (transfer_id) REFERENCES fund_transfers(id)
) ENGINE=InnoDB;

CREATE INDEX idx_transfers_customer_id ON fund_transfers(customer_id);
CREATE INDEX idx_transfers_beneficiary_id ON fund_transfers(beneficiary_id);
CREATE INDEX idx_transfers_status ON fund_transfers(status);
CREATE INDEX idx_history_transfer_id ON transfer_status_history(transfer_id);
