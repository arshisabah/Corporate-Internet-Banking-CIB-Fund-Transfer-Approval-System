package com.cib.fundtransfer.repository;

import com.cib.fundtransfer.entity.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {

    Optional<FundTransfer> findByTransferRefNo(String transferRefNo);

    List<FundTransfer> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
