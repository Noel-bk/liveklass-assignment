package com.liveklass.assignment.common.exception;

import java.time.LocalDate;

public class InvalidCoursePeriodException extends BusinessException {
    public InvalidCoursePeriodException(
        LocalDate startDate,
        LocalDate endDate
    ) {
        super(
            ErrorCode.INVALID_COURSE_PERIOD,
            "Invalid course period. startDate=" + startDate + ", endDate=" + endDate
        );
    }
}
