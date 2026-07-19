package com.liveklass.assignment.dto;

import jakarta.validation.constraints.NotNull;

public record CreateEnrollmentRequest(
    @NotNull
    Long classmateId
) {
}
