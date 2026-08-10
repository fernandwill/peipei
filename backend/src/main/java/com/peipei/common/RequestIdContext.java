package com.peipei.common;

/**
 * Thread-local holder for the current request id, populated by {@link RequestIdFilter}.
 * Used by error responses and logging to satisfy the traceability requirement (§30).
 */
public final class RequestIdContext {

    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private RequestIdContext() {
    }

    public static void set(String requestId) {
        CURRENT.set(requestId);
    }

    public static String get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}
