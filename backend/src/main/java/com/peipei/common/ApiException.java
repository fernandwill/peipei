package com.peipei.common;

/**
 * Business/API exception carrying a standard {@link ErrorCode}, mapped to the §28 error
 * envelope by {@link GlobalExceptionHandler}.
 */
public class ApiException extends RuntimeException {

    private final ErrorCode code;

    public ApiException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
