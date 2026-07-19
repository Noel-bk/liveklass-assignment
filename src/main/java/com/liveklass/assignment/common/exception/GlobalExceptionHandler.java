package com.liveklass.assignment.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        HttpStatus status = switch (e.getErrorCode()) {
            case COURSE_NOT_FOUND,
                 CREATOR_NOT_FOUND,
                 CLASSMATE_NOT_FOUND,
                 ENROLLMENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case COURSE_NOT_OPEN,
                 COURSE_FULL,
                 DUPLICATE_ENROLLMENT,
                 INVALID_ENROLLMENT_STATUS -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };

        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                e.getErrorCode().name(),
                e.getMessage()
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        String message = fieldError != null ? fieldError.getDefaultMessage() : "Invalid request.";

        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(
                ErrorCode.INVALID_REQUEST.name(),
                message
            ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(
                ErrorCode.INVALID_REQUEST.name(),
                e.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.name(),
                "Unexpected server error."
            ));
    }
}
