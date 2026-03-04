package com.example.LoanDecisionService.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.example.LoanDecisionService.enums.EmploymentType;
import com.example.LoanDecisionService.enums.RiskBand;

@Service
public class EmiComputationService {

	

	    private static final BigDecimal HUNDRED = new BigDecimal("100");

	    public BigDecimal calculateEMI(BigDecimal principal,
	                                   BigDecimal annualRate,
	                                   int tenureMonths) {

	        BigDecimal monthlyRate = annualRate
	                .divide(HUNDRED, 10, RoundingMode.HALF_UP)
	                .divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);

	        BigDecimal onePlusRPowerN =
	                monthlyRate.add(BigDecimal.ONE)
	                        .pow(tenureMonths);

	        BigDecimal numerator =
	                principal.multiply(monthlyRate)
	                        .multiply(onePlusRPowerN);

	        BigDecimal denominator =
	                onePlusRPowerN.subtract(BigDecimal.ONE);

	        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
	    }
	    
	    
	    public BigDecimal calculateFinalRate(
	            RiskBand risk,
	            EmploymentType employmentType,
	            BigDecimal loanAmount) {

	        BigDecimal rate = new BigDecimal("12"); // base rate

	        // Risk premium
	        switch (risk) {
	            case MEDIUM -> rate = rate.add(new BigDecimal("1.5"));
	            case HIGH -> rate = rate.add(new BigDecimal("3"));
	        }

	        // Employment premium
	        if (employmentType == EmploymentType.SELF_EMPLOYED) {
	            rate = rate.add(new BigDecimal("1"));
	        }

	        // Loan size premium
	        if (loanAmount.compareTo(new BigDecimal("1000000")) > 0) {
	            rate = rate.add(new BigDecimal("0.5"));
	        }

	        return rate;
	    }
	}