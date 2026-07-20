package com.cib.fundtransfer.enums;

/**
 * Workflow status of a fund transfer.
 *
 * INITIATED       -> transfer record created, not yet validated
 * VALIDATED       -> beneficiary confirmed ACTIVE, ready to submit for approval
 * PENDING_APPROVAL -> submitted to approval-service, awaiting checker action
 * APPROVED        -> checker approved; ready for execution
 * REJECTED        -> checker rejected; can be modified and resubmitted
 * SENT_BACK       -> checker requested modification; can be modified and resubmitted
 * EXECUTED        -> transfer has been executed (terminal, success)
 * FAILED          -> validation or processing failure (terminal, failure)
 */
public enum TransferStatus {
    INITIATED,
    VALIDATED,
    PENDING_APPROVAL,
    APPROVED,
    REJECTED,
    SENT_BACK,
    EXECUTED,
    FAILED
}
