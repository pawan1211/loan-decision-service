package com.example.LoanDecisionService.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.LoanDecisionService.domain.LoanModel;

public interface LoanApplicationRepository extends JpaRepository<LoanModel,UUID> 
{

}
