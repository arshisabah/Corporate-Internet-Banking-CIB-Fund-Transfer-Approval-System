package com.cib.approval.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalActionRequest {

    @NotBlank(message = "Approver ID is required")
    private String approverId;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;
}
