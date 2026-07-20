package com.cib.approval.dto.response;

import com.cib.approval.enums.ApprovalAction;
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
public class ApprovalHistoryResponse {

    private Long id;
    private Long approvalRequestId;
    private ApprovalAction action;
    private String approverId;
    private String remarks;
    private LocalDateTime actionDate;
}
