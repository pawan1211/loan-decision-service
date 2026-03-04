package com.example.LoanDecisionService.dto;

import java.math.BigDecimal;

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
public class Offer {

    private BigDecimal interestRate;

    private Integer tenureMonths;

    private BigDecimal emi;

    private BigDecimal totalPayable;
}