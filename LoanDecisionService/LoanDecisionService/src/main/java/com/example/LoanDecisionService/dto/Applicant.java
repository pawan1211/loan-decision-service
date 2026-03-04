package com.example.LoanDecisionService.dto;

import java.math.BigDecimal;

import com.example.LoanDecisionService.enums.EmploymentType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Applicant {
	
	    @NotBlank(message = "Name is required")
	    private String name;

	    @NotNull(message = "Age is required")
	    @Min(value = 21, message = "Age must be at least 21")
	    @Max(value = 60, message = "Age must not exceed 60")
	    private Integer age;

	    @NotNull(message = "Monthly income is required")
	    @DecimalMin(value = "1.0", message = "Monthly income must be greater than 0")
	    private BigDecimal monthlyIncome;

	    @NotNull(message = "Employment type is required")
	    private EmploymentType employmentType;

	    @NotNull(message = "Credit score is required")
	    @Min(value = 300, message = "Credit score must be >= 300")
	    @Max(value = 900, message = "Credit score must be <= 900")
	    private Integer creditScore;
}
