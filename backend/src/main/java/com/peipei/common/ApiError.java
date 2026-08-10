package com.peipei.common;

/**
 * Consistent error envelope returned by the API. See §28 of the spec:
 * {@code {"code": "...", "message": "...", "requestId": "..."}}.
 */
public record ApiError(String code, String message, String requestId) {
}
