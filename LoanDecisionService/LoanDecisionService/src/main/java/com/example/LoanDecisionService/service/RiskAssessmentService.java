package com.example.LoanDecisionService.service;

import org.springframework.stereotype.Service;

import com.example.LoanDecisionService.enums.RiskBand;

@Service
public class RiskAssessmentService {

    public RiskBand classify(Integer creditScore) {

        if (creditScore >= 750) return RiskBand.LOW;
        if (creditScore >= 650) return RiskBand.MEDIUM;
        return RiskBand.HIGH;
    }
}
