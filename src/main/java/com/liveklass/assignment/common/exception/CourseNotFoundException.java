package com.liveklass.assignment.common.exception;

public class CourseNotFoundException extends BusinessException {
    public CourseNotFoundException(Long id) {
        super(
            ErrorCode.COURSE_NOT_FOUND,
            "Course not found. id=" + id
        );
    }
}
