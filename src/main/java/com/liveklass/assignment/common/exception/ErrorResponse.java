package com.liveklass.assignment.common.exception;

public record ErrorResponse(
    String code,
    String message
) {
}
