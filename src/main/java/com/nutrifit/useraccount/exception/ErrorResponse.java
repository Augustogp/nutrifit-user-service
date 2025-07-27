package com.nutrifit.useraccount.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponse {

    private String message;
    private String errorCode;
    private String details; // general-purpose string detail
    private List<FieldErrorDetail> errors; // optional field-level errors
    private LocalDateTime timestamp;

    public ErrorResponse(String message, String errorCode, String details) {
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message, String errorCode, List<FieldErrorDetail> fieldErrors) {
        this.message = message;
        this.errorCode = errorCode;
        this.errors = fieldErrors;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message, String errorCode, String details, List<FieldErrorDetail> fieldErrors) {
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
        this.errors = fieldErrors;
        this.timestamp = LocalDateTime.now();
    }

    @Data
    @AllArgsConstructor
    public static class FieldErrorDetail {
        private String field;
        private String message;
    }
}
