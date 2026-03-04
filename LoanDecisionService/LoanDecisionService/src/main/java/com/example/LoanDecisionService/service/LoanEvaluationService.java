package com.example.LoanDecisionService.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.LoanDecisionService.domain.LoanModel;
import com.example.LoanDecisionService.dto.LoanApplicationRequest;
import com.example.LoanDecisionService.dto.LoanApplicationResponse;
import com.example.LoanDecisionService.dto.Offer;
import com.example.LoanDecisionService.enums.ApplicationStatus;
import com.example.LoanDecisionService.enums.RiskBand;
import com.example.LoanDecisionService.repository.LoanApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanEvaluationService {

    private final EmiComputationService emiComputationService;
    private final RiskAssessmentService riskService;
    private final LoanApplicationRepository repository;
   // private final LoanApplicationResponse loanApplicationResponse;

    public LoanApplicationResponse evaluate(LoanApplicationRequest request) {

        List<String> rejectionReasons = new ArrayList<>();

        // Convert DTO → Entity
        LoanModel entity = new LoanModel();

        entity.setName(request.getApplicant().getName());
        entity.setAge(request.getApplicant().getAge());
        entity.setMonthlyIncome(request.getApplicant().getMonthlyIncome());
        entity.setEmploymentType(request.getApplicant().getEmploymentType());
        entity.setCreditScore(request.getApplicant().getCreditScore());

        entity.setLoanAmount(request.getLoan().getAmount());
        entity.setTenureMonths(request.getLoan().getTenureMonths());
        entity.setPurpose(request.getLoan().getPurpose());

        // 1️⃣ Credit Score Check
        if (request.getApplicant().getCreditScore() < 600) {
            rejectionReasons.add("CREDIT_SCORE_TOO_LOW");
        }

        // 2️⃣ Age + Tenure Check
        int age = request.getApplicant().getAge();
        int tenureYears = request.getLoan().getTenureMonths() / 12;

        if (age + tenureYears > 65) {
            rejectionReasons.add("AGE_TENURE_LIMIT_EXCEEDED");
        }

        if (!rejectionReasons.isEmpty()) {

            entity.setStatus(ApplicationStatus.REJECTED);
            entity.setCreatedAt(LocalDateTime.now());
            repository.save(entity);   // SAVE TO DB

            return buildRejectedResponse(entity.getId(), rejectionReasons);
        }

        // 3️⃣ Risk Classification
        RiskBand riskBand =
                riskService.classify(request.getApplicant().getCreditScore());

        // 4️⃣ Interest Calculation
        BigDecimal interestRate =
                emiComputationService.calculateFinalRate(
                        riskBand,
                        request.getApplicant().getEmploymentType(),
                        request.getLoan().getAmount());

        // 5️⃣ EMI Calculation
        BigDecimal emi =
                emiComputationService.calculateEMI(
                        request.getLoan().getAmount(),
                        interestRate,
                        request.getLoan().getTenureMonths());

        // 6️⃣ Income Checks
        BigDecimal sixtyPercentIncome =
                request.getApplicant().getMonthlyIncome()
                        .multiply(new BigDecimal("0.6"));

        BigDecimal fiftyPercentIncome =
                request.getApplicant().getMonthlyIncome()
                        .multiply(new BigDecimal("0.5"));

        if (emi.compareTo(sixtyPercentIncome) > 0) {
            rejectionReasons.add("EMI_EXCEEDS_60_PERCENT");
        } else if (emi.compareTo(fiftyPercentIncome) > 0) {
            rejectionReasons.add("EMI_EXCEEDS_50_PERCENT");
        }

        if (!rejectionReasons.isEmpty()) {

            entity.setStatus(ApplicationStatus.REJECTED);
            entity.setRiskBand(riskBand);

            entity.setCreatedAt(LocalDateTime.now());
            repository.save(entity);   // SAVE REJECTED APPLICATION

            return buildRejectedResponse(entity.getId(), rejectionReasons);
        }

        // 7️⃣ Total Payable
        BigDecimal totalPayable =
                emi.multiply(BigDecimal.valueOf(request.getLoan().getTenureMonths()))
                        .setScale(2, RoundingMode.HALF_UP);

        // Save Approved Application
        entity.setStatus(ApplicationStatus.APPROVED);
    
        entity.setRiskBand(riskBand);
        entity.setEmi(emi);
        entity.setInterestRate(interestRate);
        entity.setCreatedAt(LocalDateTime.now());


        LoanModel saved = repository.save(entity);

        return buildApprovedResponse(
                saved.getId(),
                riskBand,
                interestRate,
                request.getLoan().getTenureMonths(),
                emi,
                totalPayable);
    }
    
    
    private LoanApplicationResponse buildApprovedResponse(
            UUID id,
            RiskBand riskBand,
            BigDecimal interestRate,
            Integer tenureMonths,
            BigDecimal emi,
            BigDecimal totalPayable) {

        LoanApplicationResponse response = new LoanApplicationResponse();

        response.setApplicationId(id);
        response.setStatus(ApplicationStatus.APPROVED);
        response.setRiskBand(riskBand);

        Offer offer = new Offer();

        offer.setInterestRate(interestRate.setScale(2, RoundingMode.HALF_UP));
        offer.setTenureMonths(tenureMonths);
        offer.setEmi(emi.setScale(2, RoundingMode.HALF_UP));
        offer.setTotalPayable(totalPayable.setScale(2, RoundingMode.HALF_UP));

        response.setOffer(offer);
        response.setRejectionReasons(null);

        return response;
    }
    
    
    
    private LoanApplicationResponse buildRejectedResponse(
            UUID id,
            List<String> rejectionReasons) {

        LoanApplicationResponse response = new LoanApplicationResponse();

        response.setApplicationId(id);
        response.setStatus(ApplicationStatus.REJECTED);
        response.setRiskBand(null);
        response.setOffer(null);
        response.setRejectionReasons(rejectionReasons);

        return response;
    }
}