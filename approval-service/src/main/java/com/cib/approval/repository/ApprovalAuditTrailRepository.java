package com.cib.approval.repository;

import com.cib.approval.entity.ApprovalAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalAuditTrailRepository extends JpaRepository<ApprovalAuditTrail, Long> {

    List<ApprovalAuditTrail> findByTransferIdOrderByPerformedAtAsc(Long transferId);
}
