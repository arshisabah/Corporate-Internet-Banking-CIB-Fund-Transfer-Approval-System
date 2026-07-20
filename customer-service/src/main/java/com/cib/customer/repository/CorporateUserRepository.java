package com.cib.customer.repository;

import com.cib.customer.entity.CorporateUser;
import com.cib.customer.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CorporateUserRepository extends JpaRepository<CorporateUser, Long> {

    Optional<CorporateUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<CorporateUser> findByCustomerId(Long customerId);

    List<CorporateUser> findByCustomerIdAndRole(Long customerId, UserRole role);
}
