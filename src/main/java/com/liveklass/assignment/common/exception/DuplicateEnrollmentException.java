package com.liveklass.assignment.common.exception;

public class DuplicateEnrollmentException extends BusinessException {
    public DuplicateEnrollmentException(Long courseId, Long classmateId) {
        super(
            ErrorCode.DUPLICATE_ENROLLMENT,
            "Classmate(id=" + classmateId +
                ") is already enrolled in course(id=" + courseId + ")."
        );

    }
}
