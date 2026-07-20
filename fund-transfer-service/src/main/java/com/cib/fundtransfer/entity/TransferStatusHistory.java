package com.cib.fundtransfer.entity;

import com.cib.fundtransfer.enums.TransferStatus;
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
 * Immutable audit record of a single FundTransfer status change.
 * Satisfies the "Every status change must be audited" business rule.
 * Written once per transition, never updated - no Auditable base class
 * needed here since there is no concept of "updating" a history row.
 */
@Entity
@Table(name = "transfer_status_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(name = "fk_history_transfer"))
    private FundTransfer transfer;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 20)
    private TransferStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 20)
    private TransferStatus newStatus;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "changed_by", length = 100)
    private String changedBy;

    @Column(name = "remarks", length = 500)
    private String remarks;
}
