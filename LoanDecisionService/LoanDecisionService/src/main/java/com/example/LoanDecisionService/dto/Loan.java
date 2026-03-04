package com.example.LoanDecisionService.dto;

import java.math.BigDecimal;

import com.example.LoanDecisionService.enums.LoanPurpose;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Loan {
	
	@NotNull(message = "Loan amount is required")
	@DecimalMin(value = "10000", message = "Loan amount must be at least 10,000")
	@DecimalMax(value = "5000000", message = "Loan amount must not exceed 50,00,000")
	private BigDecimal amount;

	@NotNull(message = "Tenure months is required")
	@Min(value = 6, message = "Tenure must be at least 6 months")
	@Max(value = 360, message = "Tenure must not exceed 360 months")
	private Integer tenureMonths;

	@NotNull(message = "Loan purpose is required")
	private LoanPurpose purpose;

}
