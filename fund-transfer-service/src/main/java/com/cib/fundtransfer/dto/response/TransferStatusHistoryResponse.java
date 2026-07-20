package com.cib.fundtransfer.dto.response;

import com.cib.fundtransfer.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusHistoryResponse {

    private Long id;
    private Long transferId;
    private TransferStatus oldStatus;
    private TransferStatus newStatus;
    private LocalDateTime changedAt;
    private String changedBy;
    private String remarks;
}
