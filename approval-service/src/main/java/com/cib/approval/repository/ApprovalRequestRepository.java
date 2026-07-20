package com.cib.approval.repository;

import com.cib.approval.entity.ApprovalRequest;
import com.cib.approval.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {

    List<ApprovalRequest> findByStatus(ApprovalStatus status);

    List<ApprovalRequest> findByTransferIdOrderByCreatedAtDesc(Long transferId);
}
