package com.liveklass.assignment.common.exception;

import com.liveklass.assignment.entity.EnrollmentStatus;

public class InvalidEnrollmentStatusException extends BusinessException {
    public InvalidEnrollmentStatusException(Long enrollmentId, EnrollmentStatus currentStatus) {
        super(
            ErrorCode.INVALID_ENROLLMENT_STATUS,
            "Enrollment(id=" + enrollmentId +
                ") cannot be processed. Current status is " + currentStatus + "."
        );
    }
}
