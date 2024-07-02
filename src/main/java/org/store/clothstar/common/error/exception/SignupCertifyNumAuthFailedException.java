package org.store.clothstar.common.error.exception;

import org.store.clothstar.common.error.ErrorCode;

public class SignupCertifyNumAuthFailedException extends RuntimeException {
    private final ErrorCode errorCode;

    public SignupCertifyNumAuthFailedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
