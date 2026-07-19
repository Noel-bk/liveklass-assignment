package com.liveklass.assignment.dto;

import com.liveklass.assignment.entity.Enrollment;
import com.liveklass.assignment.entity.EnrollmentStatus;

import java.time.LocalDateTime;

public record EnrollmentResponse(
    Long id,
    Long courseId,
    Long classmateId,
    EnrollmentStatus status,
    LocalDateTime confirmedAt,
    LocalDateTime cancelledAt
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
            enrollment.getId(),
            enrollment.getCourse().getId(),
            enrollment.getClassmate().getId(),
            enrollment.getStatus(),
            enrollment.getConfirmedAt(),
            enrollment.getCancelledAt()
        );
    }
}
