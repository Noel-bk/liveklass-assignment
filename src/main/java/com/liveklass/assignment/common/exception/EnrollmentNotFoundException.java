package com.liveklass.assignment.common.exception;

public class EnrollmentNotFoundException extends BusinessException {
    public EnrollmentNotFoundException(Long id) {
        super(
            ErrorCode.ENROLLMENT_NOT_FOUND,
            "Enrollment not found: id=" + id
        );
    }
}
