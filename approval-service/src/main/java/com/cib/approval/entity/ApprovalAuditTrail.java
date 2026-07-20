package com.cib.approval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Immutable, append-only compliance audit log covering every event in the
 * approval lifecycle (request created, approved, rejected, sent back).
 * Deliberately kept as a separate table from ApprovalHistory: history is a
 * business view scoped to a single ApprovalRequest, while the audit trail
 * is a flat, transferId-scoped log intended for compliance/regulatory
 * review and is never updated once written.
 */
@Entity
@Table(name = "approval_audit_trail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalAuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_id", nullable = false)
    private Long transferId;

    @Column(name = "action", nullable = false, length = 30)
    private String action;

    @Column(name = "performed_by", nullable = false, length = 100)
    private String performedBy;

    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;

    @Column(name = "details", length = 500)
    private String details;
}
