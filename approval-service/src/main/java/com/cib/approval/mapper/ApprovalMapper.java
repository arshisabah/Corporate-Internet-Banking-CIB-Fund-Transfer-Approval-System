package com.cib.approval.mapper;

import com.cib.approval.dto.response.ApprovalHistoryResponse;
import com.cib.approval.dto.response.ApprovalResponse;
import com.cib.approval.dto.response.AuditTrailResponse;
import com.cib.approval.entity.ApprovalAuditTrail;
import com.cib.approval.entity.ApprovalHistory;
import com.cib.approval.entity.ApprovalRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApprovalMapper {

    ApprovalResponse toResponse(ApprovalRequest entity);

    @Mapping(target = "approvalRequestId", source = "approvalRequest.id")
    ApprovalHistoryResponse toHistoryResponse(ApprovalHistory entity);

    AuditTrailResponse toAuditTrailResponse(ApprovalAuditTrail entity);
}
