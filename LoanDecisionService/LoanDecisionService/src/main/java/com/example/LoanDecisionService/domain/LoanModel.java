package com.example.LoanDecisionService.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

//import org.hibernate.validator.constraints.UUID;

import com.example.LoanDecisionService.enums.ApplicationStatus;
import com.example.LoanDecisionService.enums.EmploymentType;
import com.example.LoanDecisionService.enums.LoanPurpose;
import com.example.LoanDecisionService.enums.RiskBand;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LoanModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String name;
    private Integer age;
    private BigDecimal monthlyIncome;
    private EmploymentType employmentType;
    private Integer creditScore;

    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private LoanPurpose purpose;
    

    private RiskBand riskBand;

    private ApplicationStatus status;



    private BigDecimal emi;
    private BigDecimal interestRate;

    private String rejectionReasons;

    private LocalDateTime createdAt;

}
