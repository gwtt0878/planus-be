package com.planus.auth;

public class UserContext {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getUserId() {
        return userIdHolder.get();
    }

    public static void clearUserId() {
        userIdHolder.remove();
    }
}