package com.cib.fundtransfer.dto.request;

import com.cib.fundtransfer.enums.TransferStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Callback contract used by approval-service (via its own Feign client
 * pointed at fund-transfer-service) to push a status change after a
 * checker action (APPROVE / REJECT / SEND_BACK) or after execution.
 * fund-transfer-service remains the single source of truth for its own
 * status - approval-service requests the change, this service validates
 * and applies it via TransferStatusValidator.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransferStatusRequest {

    @NotNull(message = "New status is required")
    private TransferStatus newStatus;

    @NotBlank(message = "changedBy is required")
    private String changedBy;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;
}
