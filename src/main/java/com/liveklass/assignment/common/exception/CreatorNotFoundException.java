package com.liveklass.assignment.common.exception;

public class CreatorNotFoundException extends BusinessException {
    public CreatorNotFoundException(Long id) {
        super(
            ErrorCode.CREATOR_NOT_FOUND,
            "Creator not found. id=" + id
        );
    }
}
