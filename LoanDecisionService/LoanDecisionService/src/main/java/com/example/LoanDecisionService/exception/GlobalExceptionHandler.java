package com.example.LoanDecisionService.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

   
	
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Validation Failed");
        response.put("messages", errors);

        return ResponseEntity.badRequest().body(response);
    }

//    @ExceptionHandler(BusinessException.class)
//    public ResponseEntity<?> handleBusiness(BusinessException ex) {
//
//        return ResponseEntity.badRequest()
//                .body(new ErrorResponse(ex.getErrorCode(), List.of(ex.getMessage())));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleGeneric(Exception ex) {
//
//        return ResponseEntity.internalServerError()
//                .body(new ErrorResponse("INTERNAL_ERROR",
//                        List.of("Something went wrong")));
//    }
}
