package com.cib.fundtransfer.dto.response;

import com.cib.fundtransfer.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private Long id;
    private String transferRefNo;
    private Long customerId;
    private Long beneficiaryId;
    private BigDecimal amount;
    private String currency;
    private String remarks;
    private TransferStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
