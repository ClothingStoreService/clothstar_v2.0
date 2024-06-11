package org.store.clothstar.common.error.exception;

import org.store.clothstar.common.error.ErrorCode;

public class RefreshTokenInValidException extends RuntimeException {
    private final ErrorCode errorCode;

    public RefreshTokenInValidException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
