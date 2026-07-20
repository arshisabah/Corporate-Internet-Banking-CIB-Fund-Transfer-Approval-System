package com.cib.common.exception;

import com.cib.common.enums.ErrorCode;
import lombok.Getter;

/**
 * Base unchecked exception for all domain/business rule violations.
 * Every service-specific exception should extend this so GlobalExceptionHandler
 * can process them uniformly.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
