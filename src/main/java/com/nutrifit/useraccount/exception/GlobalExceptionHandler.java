package com.nutrifit.useraccount.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_ARGUMENT",
                e.getMessage(),
                "An invalid argument was provided."
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {

        List<ErrorResponse.FieldErrorDetail> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse.FieldErrorDetail(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                "Validation failed",
                "VALIDATION_ERROR",
                "One or more validation errors occurred.",
                fieldErrors
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "User not found",
                "RESOURCE_NOT_FOUND",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getCause();

        String message = "A data integrity violation occurred.";
        String errorCode = "DATA_INTEGRITY_ERROR";

        if (rootCause instanceof ConstraintViolationException constraintEx) {
            String constraintName = constraintEx.getConstraintName();

            if (constraintName != null) {
                if (constraintName.contains("user_account.username")) {
                    message = "Username already exists.";
                    errorCode = "USERNAME_EXISTS";
                } else if (constraintName.contains("user_account.email")) {
                    message = "Email already exists.";
                    errorCode = "EMAIL_EXISTS";
                } else {
                    message = "Constraint violation: " + constraintName;
                }
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(
                message,
                errorCode,
                ex.getMostSpecificCause().getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
