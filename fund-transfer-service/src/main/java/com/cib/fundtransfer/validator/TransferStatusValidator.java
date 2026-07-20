package com.cib.fundtransfer.validator;

import com.cib.common.exception.InvalidStateTransitionException;
import com.cib.fundtransfer.enums.TransferStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.cib.fundtransfer.enums.TransferStatus.APPROVED;
import static com.cib.fundtransfer.enums.TransferStatus.EXECUTED;
import static com.cib.fundtransfer.enums.TransferStatus.FAILED;
import static com.cib.fundtransfer.enums.TransferStatus.INITIATED;
import static com.cib.fundtransfer.enums.TransferStatus.PENDING_APPROVAL;
import static com.cib.fundtransfer.enums.TransferStatus.REJECTED;
import static com.cib.fundtransfer.enums.TransferStatus.SENT_BACK;
import static com.cib.fundtransfer.enums.TransferStatus.VALIDATED;

/**
 * Single source of truth for legal FundTransfer status transitions.
 * Every status-changing operation in FundTransferServiceImpl must go through
 * validateTransition() before persisting a new status - this is what
 * satisfies the "Prevent invalid workflow transitions" business rule.
 *
 * Workflow:
 *   INITIATED -> VALIDATED -> PENDING_APPROVAL -> APPROVED -> EXECUTED
 *                    |               |-> REJECTED  -> PENDING_APPROVAL (resubmit)
 *                    |               |-> SENT_BACK  -> PENDING_APPROVAL (resubmit)
 *                    |-> FAILED (beneficiary not active)
 */
@Component
public class TransferStatusValidator {

    private static final Map<TransferStatus, Set<TransferStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(TransferStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(INITIATED, EnumSet.of(VALIDATED, FAILED));
        ALLOWED_TRANSITIONS.put(VALIDATED, EnumSet.of(PENDING_APPROVAL, FAILED));
        ALLOWED_TRANSITIONS.put(PENDING_APPROVAL, EnumSet.of(APPROVED, REJECTED, SENT_BACK));
        // Rejected / sent-back transfers can only re-enter the approval queue after modification
        ALLOWED_TRANSITIONS.put(REJECTED, EnumSet.of(PENDING_APPROVAL));
        ALLOWED_TRANSITIONS.put(SENT_BACK, EnumSet.of(PENDING_APPROVAL));
        ALLOWED_TRANSITIONS.put(APPROVED, EnumSet.of(EXECUTED, FAILED));
        // EXECUTED and FAILED are terminal - no outbound transitions
        ALLOWED_TRANSITIONS.put(EXECUTED, EnumSet.noneOf(TransferStatus.class));
        ALLOWED_TRANSITIONS.put(FAILED, EnumSet.noneOf(TransferStatus.class));
    }

    /**
     * Validates that moving from currentStatus to targetStatus is a legal
     * transition. Throws InvalidStateTransitionException (HTTP 409) if not.
     */
    public void validateTransition(TransferStatus currentStatus, TransferStatus targetStatus) {
        Set<TransferStatus> allowedNextStates = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowedNextStates.contains(targetStatus)) {
            throw new InvalidStateTransitionException(currentStatus.name(),
                    "TRANSITION_TO_" + targetStatus.name());
        }
    }
}
