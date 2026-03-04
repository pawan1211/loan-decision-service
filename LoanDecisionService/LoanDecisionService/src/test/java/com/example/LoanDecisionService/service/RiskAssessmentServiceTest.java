package com.example.LoanDecisionService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.LoanDecisionService.enums.RiskBand;

@SpringBootTest
class RiskAssessmentServiceTest {

    @Autowired
    private RiskAssessmentService riskService;

    @Test
    void shouldReturnLowRisk() {

        RiskBand risk = riskService.classify(780);

        assertEquals(RiskBand.LOW, risk);
    }

    @Test
    void shouldReturnMediumRisk() {

        RiskBand risk = riskService.classify(720);

        assertEquals(RiskBand.MEDIUM, risk);
    }

    @Test
    void shouldReturnHighRisk() {

        RiskBand risk = riskService.classify(620);

        assertEquals(RiskBand.HIGH, risk);
    }
}