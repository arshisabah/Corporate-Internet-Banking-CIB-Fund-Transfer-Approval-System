package com.cib.beneficiary.dto.response;

import com.cib.beneficiary.enums.BeneficiaryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryResponse {

    private Long id;
    private Long customerId;
    private String beneficiaryName;
    private String accountNumber;
    private String bankName;
    private String ifscCode;
    private BeneficiaryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
