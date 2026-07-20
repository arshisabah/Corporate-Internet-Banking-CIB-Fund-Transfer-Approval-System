package com.cib.common.exception;

import com.cib.common.enums.ErrorCode;

/**
 * Thrown when a requested entity (Customer, Beneficiary, Transfer, Approval ...)
 * cannot be found by its identifier. Mapped to HTTP 404 by GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String entityName, Object identifier) {
        super(ErrorCode.RESOURCE_NOT_FOUND,
                String.format("%s not found with identifier: %s", entityName, identifier));
    }

    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
