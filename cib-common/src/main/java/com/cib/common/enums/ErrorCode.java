package com.cib.common.enums;

import org.springframework.http.HttpStatus;

/**
 * Central catalog of business error codes used across all microservices.
 * Keeping this in cib-common ensures consistent error codes/messages
 * regardless of which service raises the exception.
 */
public enum ErrorCode {

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested resource was not found"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "Resource already exists"),
    INVALID_STATE_TRANSITION(HttpStatus.CONFLICT, "Invalid workflow state transition"),
    BENEFICIARY_NOT_ACTIVE(HttpStatus.UNPROCESSABLE_ENTITY, "Beneficiary is not ACTIVE and cannot receive transfers"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Request validation failed"),
    UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "User is not authorized to perform this action"),
    DOWNSTREAM_SERVICE_ERROR(HttpStatus.BAD_GATEWAY, "Downstream service call failed"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected internal error occurred");

    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
