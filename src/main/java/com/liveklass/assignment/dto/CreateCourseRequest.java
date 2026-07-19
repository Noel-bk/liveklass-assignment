package com.liveklass.assignment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCourseRequest(
    @NotNull
    Long creatorId,

    @NotBlank
    @Size(max = 100)
    String title,

    String description,

    @NotNull
    BigDecimal price,

    @NotNull
    @Min(1)
    Integer capacity,

    @NotNull
    LocalDate startDate,

    @NotNull
    LocalDate endDate
) {
}
