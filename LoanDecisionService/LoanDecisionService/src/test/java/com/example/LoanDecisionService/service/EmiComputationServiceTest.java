package com.example.LoanDecisionService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmiComputationServiceTest {

    @Autowired
    private EmiComputationService emiService;

    @Test
    void shouldCalculateCorrectEmi() {

        BigDecimal emi = emiService.calculateEMI(
                new BigDecimal("500000"),
                new BigDecimal("13.5"),
                36
        );

        assertEquals(new BigDecimal("16967.64"), emi);
    }
}
