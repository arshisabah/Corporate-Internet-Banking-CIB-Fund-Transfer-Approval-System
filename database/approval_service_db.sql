-- =========================================================================
-- approval_service_db
-- transfer_id is a plain reference into fund_transfer_service_db.fund_transfers.id
-- - NOT a foreign key, since these are separate databases.
-- =========================================================================

CREATE DATABASE IF NOT EXISTS approval_service_db;
USE approval_service_db;

CREATE TABLE approval_requests (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    transfer_id       BIGINT         NOT NULL,
    transfer_ref_no   VARCHAR(40)    NOT NULL,
    customer_id       BIGINT         NOT NULL,
    amount            DECIMAL(19,2)  NOT NULL,
    currency          VARCHAR(3)     NOT NULL,
    status            VARCHAR(20)    NOT NULL,
    created_at        DATETIME       NOT NULL,
    updated_at        DATETIME,
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100),
    CONSTRAINT chk_approval_status CHECK (status IN ('PENDING','APPROVED','REJECTED','SENT_BACK'))
) ENGINE=InnoDB;

CREATE TABLE approval_history (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_request_id   BIGINT        NOT NULL,
    action                VARCHAR(20)   NOT NULL,
    approver_id           VARCHAR(100)  NOT NULL,
    remarks               VARCHAR(500),
    action_date           DATETIME      NOT NULL,
    CONSTRAINT fk_history_approval_request FOREIGN KEY (approval_request_id) REFERENCES approval_requests(id),
    CONSTRAINT chk_approval_action CHECK (action IN ('APPROVE','REJECT','SEND_BACK'))
) ENGINE=InnoDB;

CREATE TABLE approval_audit_trail (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    transfer_id    BIGINT        NOT NULL,
    action         VARCHAR(30)   NOT NULL,
    performed_by   VARCHAR(100)  NOT NULL,
    performed_at   DATETIME      NOT NULL,
    details        VARCHAR(500)
) ENGINE=InnoDB;

CREATE INDEX idx_approval_requests_status ON approval_requests(status);
CREATE INDEX idx_approval_requests_transfer_id ON approval_requests(transfer_id);
CREATE INDEX idx_approval_history_request_id ON approval_history(approval_request_id);
CREATE INDEX idx_audit_trail_transfer_id ON approval_audit_trail(transfer_id);
