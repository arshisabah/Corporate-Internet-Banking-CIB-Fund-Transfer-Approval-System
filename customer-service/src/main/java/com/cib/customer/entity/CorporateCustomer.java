package com.cib.customer.entity;

import com.cib.common.entity.Auditable;
import com.cib.customer.enums.CustomerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a corporate customer (company/organization) onboarded onto the
 * Internet Banking platform. A single CorporateCustomer owns many
 * CorporateUser accounts and, across services, many Beneficiaries and
 * FundTransfers (referenced there by customerId only - no cross-service FK).
 */
@Entity
@Table(name = "corporate_customers", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cif_number", columnNames = "cif_number"),
        @UniqueConstraint(name = "uk_registration_number", columnNames = "registration_number")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorporateCustomer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    /** Customer Information File number - unique bank-assigned corporate identifier */
    @Column(name = "cif_number", nullable = false, length = 20)
    private String cifNumber;

    @Column(name = "registration_number", nullable = false, length = 50)
    private String registrationNumber;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "address", length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CustomerStatus status;
}
