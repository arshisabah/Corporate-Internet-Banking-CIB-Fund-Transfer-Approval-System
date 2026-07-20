package com.cib.common.exception;

import com.cib.common.enums.ErrorCode;

/**
 * Thrown whenever a workflow action would move an entity (e.g. FundTransfer)
 * into a status that is not a legal transition from its current status.
 * Example: attempting to APPROVE a transfer that is already EXECUTED.
 */
public class InvalidStateTransitionException extends BusinessException {

    public InvalidStateTransitionException(String currentState, String attemptedAction) {
        super(ErrorCode.INVALID_STATE_TRANSITION,
                String.format("Cannot perform action '%s' while entity is in state '%s'",
                        attemptedAction, currentState));
    }
}
