package com.cib.approval.repository;

import com.cib.approval.entity.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {

    List<ApprovalHistory> findByApprovalRequestIdOrderByActionDateAsc(Long approvalRequestId);
}
