package com.cib.fundtransfer.repository;

import com.cib.fundtransfer.entity.TransferStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferStatusHistoryRepository extends JpaRepository<TransferStatusHistory, Long> {

    List<TransferStatusHistory> findByTransferIdOrderByChangedAtAsc(Long transferId);
}
