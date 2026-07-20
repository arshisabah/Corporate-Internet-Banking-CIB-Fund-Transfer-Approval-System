package com.cib.fundtransfer.entity;

import com.cib.common.entity.Auditable;
import com.cib.fundtransfer.enums.TransferStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents a single fund transfer instruction. customerId and beneficiaryId
 * are plain references into customer-service and beneficiary-service
 * respectively (database-per-service - no cross-DB foreign keys); referential
 * integrity for beneficiaryId is enforced via a Feign validation call at
 * creation time, not a database constraint.
 */
@Entity
@Table(name = "fund_transfers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundTransfer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_ref_no", nullable = false, unique = true, length = 40)
    private String transferRefNo;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransferStatus status;
}
