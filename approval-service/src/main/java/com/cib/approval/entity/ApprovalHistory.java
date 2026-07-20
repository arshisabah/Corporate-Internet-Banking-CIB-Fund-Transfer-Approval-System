package com.cib.approval.entity;

import com.cib.approval.enums.ApprovalAction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Business-facing record of a checker action (APPROVE / REJECT / SEND_BACK)
 * taken on an ApprovalRequest. Satisfies "Maintain approval history" -
 * intended for display in a "who approved/rejected this and when" UI.
 */
@Entity
@Table(name = "approval_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_request_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(name = "fk_history_approval_request"))
    private ApprovalRequest approvalRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private ApprovalAction action;

    @Column(name = "approver_id", nullable = false, length = 100)
    private String approverId;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;
}
