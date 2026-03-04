package com.example.LoanDecisionService.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

import com.example.LoanDecisionService.enums.ApplicationStatus;
import com.example.LoanDecisionService.enums.RiskBand;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponse {

	private UUID applicationId;

    private ApplicationStatus status;

    private RiskBand riskBand;

    private Offer offer;

    private List<String> rejectionReasons;

   
}