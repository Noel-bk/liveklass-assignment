package com.liveklass.assignment.common.exception;

public class CancellationPeriodExpiredException extends BusinessException {
    public CancellationPeriodExpiredException(Long enrollmentId) {
        super(
            ErrorCode.CANCELLATION_PERIOD_EXPIRED,
            "Cancellation period expired. enrollmentId=" + enrollmentId
        );
    }
}
