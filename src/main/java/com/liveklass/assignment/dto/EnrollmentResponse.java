package com.liveklass.assignment.dto;

import com.liveklass.assignment.entity.Enrollment;
import com.liveklass.assignment.entity.EnrollmentStatus;

import java.time.LocalDateTime;

public record EnrollmentResponse(
    Long enrollmentId,
    Long courseId,
    String courseTitle,
    String courseDescription,
    EnrollmentStatus status,
    LocalDateTime enrolledAt,
    LocalDateTime confirmedAt,
    LocalDateTime cancelledAt
) {

    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
            enrollment.getId(),
            enrollment.getCourse().getId(),
            enrollment.getCourse().getTitle(),
            enrollment.getCourse().getDescription(),
            enrollment.getStatus(),
            enrollment.getCreatedAt(),
            enrollment.getConfirmedAt(),
            enrollment.getCancelledAt()
        );
    }
}
