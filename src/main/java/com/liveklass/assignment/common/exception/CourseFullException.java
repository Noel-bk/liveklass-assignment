package com.liveklass.assignment.common.exception;

public class CourseFullException extends BusinessException {
    public CourseFullException(Long id) {
        super(
            ErrorCode.COURSE_FULL,
            "Course is full. id=" + id
        );
    }
}
