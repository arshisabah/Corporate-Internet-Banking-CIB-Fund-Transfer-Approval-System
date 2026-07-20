package com.cib.customer.repository;

import com.cib.customer.entity.CorporateCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorporateCustomerRepository extends JpaRepository<CorporateCustomer, Long> {

    Optional<CorporateCustomer> findByCifNumber(String cifNumber);

    boolean existsByCifNumber(String cifNumber);

    boolean existsByRegistrationNumber(String registrationNumber);
}
