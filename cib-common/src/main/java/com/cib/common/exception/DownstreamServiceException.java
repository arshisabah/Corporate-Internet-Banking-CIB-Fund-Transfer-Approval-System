package com.cib.common.exception;

import com.cib.common.enums.ErrorCode;

/**
 * Wraps failures from Feign calls to downstream services (e.g. fund-transfer-service
 * calling beneficiary-service). Mapped to HTTP 502 Bad Gateway.
 */
public class DownstreamServiceException extends BusinessException {

    public DownstreamServiceException(String serviceName, String reason) {
        super(ErrorCode.DOWNSTREAM_SERVICE_ERROR,
                String.format("Call to '%s' failed: %s", serviceName, reason));
    }
}
