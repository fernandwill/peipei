package com.peipei.common;

import org.springframework.http.HttpStatus;

/**
 * Standard error codes and their HTTP statuses, mirroring the table in §28 of the spec.
 */
public enum ErrorCode {

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    INVALID_CURRENCY(HttpStatus.BAD_REQUEST),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST),
    INVALID_STATE_TRANSITION(HttpStatus.CONFLICT),
    INSUFFICIENT_REFUND_BALANCE(HttpStatus.CONFLICT),
    IDEMPOTENCY_KEY_CONFLICT(HttpStatus.CONFLICT),
    MERCHANT_SUSPENDED(HttpStatus.FORBIDDEN),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND),
    REFUND_NOT_FOUND(HttpStatus.NOT_FOUND),
    API_KEY_NOT_FOUND(HttpStatus.NOT_FOUND),
    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS),
    WEBHOOK_SIGNATURE_INVALID(HttpStatus.BAD_REQUEST),
    PAYMENT_EXPIRED(HttpStatus.GONE),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus status() {
        return status;
    }
}
