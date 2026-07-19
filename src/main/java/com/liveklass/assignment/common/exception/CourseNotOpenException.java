package com.liveklass.assignment.common.exception;

public class CourseNotOpenException extends BusinessException {
    public CourseNotOpenException(Long id) {
        super(
            ErrorCode.COURSE_NOT_OPEN,
            "Course is not open. id=" + id
        );
    }
}
