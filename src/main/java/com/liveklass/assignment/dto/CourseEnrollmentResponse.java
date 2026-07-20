package com.liveklass.assignment.dto;

import com.liveklass.assignment.entity.Enrollment;
import com.liveklass.assignment.entity.EnrollmentStatus;

import java.time.LocalDateTime;

public record CourseEnrollmentResponse(
    Long enrollmentId,
    Long classmateId,
    String classmateName,
    EnrollmentStatus status,
    LocalDateTime createdAt,
    LocalDateTime confirmedAt,
    LocalDateTime cancelledAt
) {
    public static CourseEnrollmentResponse from(Enrollment enrollment) {
        return new CourseEnrollmentResponse(
            enrollment.getId(),
            enrollment.getClassmate().getId(),
            enrollment.getClassmate().getName(),
            enrollment.getStatus(),
            enrollment.getCreatedAt(),
            enrollment.getConfirmedAt(),
            enrollment.getCancelledAt()
        );
    }
}
