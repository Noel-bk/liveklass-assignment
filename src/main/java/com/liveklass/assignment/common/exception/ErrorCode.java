package com.liveklass.assignment.common.exception;

public enum ErrorCode {
    // Not Found
    COURSE_NOT_FOUND,
    CREATOR_NOT_FOUND,
    CLASSMATE_NOT_FOUND,
    ENROLLMENT_NOT_FOUND,

    COURSE_NOT_OPEN,
    COURSE_FULL,
    DUPLICATE_ENROLLMENT,
    INVALID_ENROLLMENT_STATUS,
    CANCELLATION_PERIOD_EXPIRED,

    // Bad Request
    INVALID_REQUEST,

    // Server Error
    INTERNAL_SERVER_ERROR
}
