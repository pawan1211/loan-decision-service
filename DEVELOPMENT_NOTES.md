1. Overall Approach

The goal of this service is to evaluate loan applications and determine whether a loan offer can be approved based on predefined business rules.

The application was built using Spring Boot with a layered architecture, separating responsibilities into different components:

Controller Layer

Handles HTTP requests

Accepts loan application payload

Performs request validation using Jakarta Validation

Service Layer

Contains the core business logic for loan evaluation

Performs eligibility checks, risk classification, and EMI calculation

Repository Layer

Uses Spring Data JPA to persist loan applications and decisions

Domain Layer

Contains the LoanModel entity used for database persistence

DTO Layer

Used for request and response payloads

Keeps API models separate from database entities

The evaluation flow works as follows:

Validate request fields

Apply eligibility rules

Classify risk band

Calculate final interest rate

Calculate EMI

Check income eligibility

Generate approved or rejected response

Persist decision for audit purposes

2. Key Design Decisions
1. Layered Architecture

The application follows a Controller → Service → Repository structure to keep concerns separated.

Benefits:

Easier testing

Better maintainability

Clear business logic boundaries

2. DTO and Entity Separation

DTOs are used for:

Request validation

API communication

Entities are used for:

Database persistence

This prevents API models from leaking into the database layer.

3. Use of BigDecimal for Financial Calculations

All financial calculations such as:

EMI

Interest rates

Total payable

use BigDecimal instead of floating point numbers to avoid precision errors.

4. Risk Classification Service

Risk band classification is isolated into a separate service:

RiskAssessmentService

This improves:

Readability

Reusability

Testability

5. Dedicated EMI Calculation Service

Financial computation logic is placed in:

EmiComputationService

This ensures business rules are separate from calculation logic.

6. Audit Persistence

Every loan decision (approved or rejected) is stored in the database using:

LoanModel

Stored fields include:

Applicant information

Loan details

Risk band

Status

EMI and interest rate

Rejection reasons

Timestamp

This supports auditability and future analytics.

3. Trade-offs Considered
Simplicity vs Overengineering

For this assignment:

The design prioritizes clarity and maintainability

Avoided introducing unnecessary complexity like:

Domain-driven design layers

CQRS

Event-driven architecture

These could be considered in larger systems.

Builder vs Simple Object Creation

While Lombok @Builder was used for DTOs, service responses were constructed using simple setters to keep the code easier to follow.

Validation Location

Validation is handled using Jakarta Validation annotations in DTOs instead of manual checks in services.

This ensures invalid requests are rejected early.

4. Assumptions Made

The following assumptions were made based on the problem description:

Credit score below 600 is automatically rejected

EMI must be ≤ 60% of monthly income

Loan offers are only generated if EMI ≤ 50% of income

Only one loan offer is generated using the requested tenure

Interest rate adjustments follow:

Risk premium

Employment premium

Loan size premium

All financial values are rounded to 2 decimal places using HALF_UP

5. Improvements With More Time

If more development time were available, the following improvements would be implemented:

1. Add Integration Tests

Currently unit tests cover:

EMI calculation

Risk classification

Loan evaluation

Integration tests could verify:

Controller + service interaction

Database persistence

2. Add Logging

Introduce structured logging using SLF4J / Logback for:

Loan application submissions

Approval/rejection decisions

Error handling

3. Add API Documentation

Use Swagger / OpenAPI to automatically generate API documentation.

4. Add Database Migrations

Use Flyway or Liquibase instead of ddl-auto=update for production-grade schema management.

5. Improve Error Handling

Introduce structured error responses with:

error codes

timestamps

request identifiers

6. Add Rate Limiting

For production environments, rate limiting could be introduced to protect the API.

7. Add Caching

Risk band rules or interest rules could be cached if they become configurable in the future.

8. Introduce Configuration-Based Rules

Eligibility rules could be moved to configuration so that business teams can change thresholds without redeploying.

6. Testing Strategy

Unit tests were implemented for:

EMI calculation

Risk band classification

Loan evaluation approval

Loan rejection scenarios

These tests ensure correctness of the core business logic.

7. Technology Stack

Java 17

Spring Boot

Spring Data JPA

PostgreSQL

Jakarta Validation

Lombok

JUnit 5
