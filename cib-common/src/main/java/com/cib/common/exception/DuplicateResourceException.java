package com.cib.common.exception;

import com.cib.common.enums.ErrorCode;

/**
 * Thrown when attempting to create a resource that violates a uniqueness
 * business rule (e.g. duplicate CIF number, duplicate username, duplicate
 * beneficiary account+bank combination). Mapped to HTTP 409 Conflict.
 */
public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message) {
        super(ErrorCode.DUPLICATE_RESOURCE, message);
    }
}
