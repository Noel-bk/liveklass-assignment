package com.liveklass.assignment.common.exception;

public class ClassmateNotFoundException extends BusinessException {
    public ClassmateNotFoundException(Long id) {
        super(
            ErrorCode.CLASSMATE_NOT_FOUND,
            "Classmate not found. id=" + id
        );
    }
}
