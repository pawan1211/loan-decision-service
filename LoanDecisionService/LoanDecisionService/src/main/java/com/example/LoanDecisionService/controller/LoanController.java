package com.example.LoanDecisionService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LoanDecisionService.dto.LoanApplicationRequest;
import com.example.LoanDecisionService.dto.LoanApplicationResponse;
import com.example.LoanDecisionService.service.LoanEvaluationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class LoanController {

    private final LoanEvaluationService service;

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> create(
            @Valid @RequestBody LoanApplicationRequest request) {

        LoanApplicationResponse response = service.evaluate(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
