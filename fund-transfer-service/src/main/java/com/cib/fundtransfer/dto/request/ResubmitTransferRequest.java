package com.cib.fundtransfer.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Payload for resubmitting a REJECTED or SENT_BACK transfer. Beneficiary
 * and customer cannot change on resubmit (that would be a new transfer);
 * only amount and remarks may be modified, matching the business rule
 * "rejected transfers cannot execute until modified and resubmitted."
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResubmitTransferRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;
}
