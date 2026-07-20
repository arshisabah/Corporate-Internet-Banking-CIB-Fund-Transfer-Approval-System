package com.cib.fundtransfer.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestResponse {
    private Long approvalId;
    private Long transferId;
    private String status;
}
