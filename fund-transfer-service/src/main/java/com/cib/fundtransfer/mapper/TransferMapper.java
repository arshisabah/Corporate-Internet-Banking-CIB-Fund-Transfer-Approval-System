package com.cib.fundtransfer.mapper;

import com.cib.fundtransfer.dto.response.TransferResponse;
import com.cib.fundtransfer.dto.response.TransferStatusHistoryResponse;
import com.cib.fundtransfer.entity.FundTransfer;
import com.cib.fundtransfer.entity.TransferStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    TransferResponse toResponse(FundTransfer entity);

    @Mapping(target = "transferId", source = "transfer.id")
    TransferStatusHistoryResponse toHistoryResponse(TransferStatusHistory entity);
}
