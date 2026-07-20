package com.cib.fundtransfer.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Request payload POSTed to approval-service (POST /api/v1/approvals/requests)
 * to enqueue a transfer for checker action. Mirrors the request contract
 * approval-service exposes.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApprovalRequestDto {
    private Long transferId;
    private String transferRefNo;
    private Long customerId;
    private BigDecimal amount;
    private String currency;
}
