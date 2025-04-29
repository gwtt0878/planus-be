package com.planus.common.exception;

import lombok.Getter;

@Getter
public class NeedNicknameException extends RuntimeException {
    private final String email;
    private final String tempToken;

    public NeedNicknameException(String message, String email, String tempToken) {
        super(message);
        this.email = email;
        this.tempToken = tempToken;
    }
}
