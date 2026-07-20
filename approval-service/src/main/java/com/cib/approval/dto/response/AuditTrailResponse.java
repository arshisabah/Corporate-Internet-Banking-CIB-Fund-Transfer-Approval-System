package com.cib.approval.dto.response;

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
public class AuditTrailResponse {

    private Long id;
    private Long transferId;
    private String action;
    private String performedBy;
    private LocalDateTime performedAt;
    private String details;
}
