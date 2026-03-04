package com.example.LoanDecisionService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.LoanDecisionService.dto.Applicant;
import com.example.LoanDecisionService.dto.Loan;
import com.example.LoanDecisionService.dto.LoanApplicationRequest;
import com.example.LoanDecisionService.dto.LoanApplicationResponse;
import com.example.LoanDecisionService.enums.ApplicationStatus;
import com.example.LoanDecisionService.enums.EmploymentType;
import com.example.LoanDecisionService.enums.LoanPurpose;
import com.example.LoanDecisionService.enums.RiskBand;

@SpringBootTest
class LoanEvaluationServiceTest {

    @Autowired
    private LoanEvaluationService service;

    @Test
    void shouldRejectWhenCreditScoreLow() {

        LoanApplicationRequest request = new LoanApplicationRequest();

        Applicant applicant = new Applicant();
        applicant.setAge(30);
        applicant.setCreditScore(550);
        applicant.setMonthlyIncome(new BigDecimal("75000"));
        applicant.setEmploymentType(EmploymentType.SALARIED);

        Loan loan = new Loan();
        loan.setAmount(new BigDecimal("500000"));
        loan.setTenureMonths(36);
        loan.setPurpose(LoanPurpose.PERSONAL);

        request.setApplicant(applicant);
        request.setLoan(loan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.REJECTED, response.getStatus());
    }
    
    
    
    @Test
    void shouldApproveLoanWhenAllConditionsValid() {

        LoanApplicationRequest request = new LoanApplicationRequest();

        // Applicant
        Applicant applicant = new Applicant();
        applicant.setName("Pawan");
        applicant.setAge(30);
        applicant.setCreditScore(720);
        applicant.setMonthlyIncome(new BigDecimal("75000"));
        applicant.setEmploymentType(EmploymentType.SALARIED);

        // Loan
        Loan loan = new Loan();
        loan.setAmount(new BigDecimal("500000"));
        loan.setTenureMonths(36);
        loan.setPurpose(LoanPurpose.PERSONAL);

        request.setApplicant(applicant);
        request.setLoan(loan);

        LoanApplicationResponse response = service.evaluate(request);

        // Assertions
        assertEquals(ApplicationStatus.APPROVED, response.getStatus());
        assertEquals(RiskBand.MEDIUM, response.getRiskBand());
        assertNotNull(response.getOffer());
        assertEquals(new BigDecimal("13.50"), response.getOffer().getInterestRate());
        assertEquals(36, response.getOffer().getTenureMonths());
        assertNotNull(response.getOffer().getEmi());
    }
}
