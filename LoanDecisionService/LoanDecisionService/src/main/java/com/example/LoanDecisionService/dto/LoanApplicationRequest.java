package com.example.LoanDecisionService.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class LoanApplicationRequest {
	
	@Valid
    private Applicant applicant;

	 @Valid
	 private Loan loan;

}
