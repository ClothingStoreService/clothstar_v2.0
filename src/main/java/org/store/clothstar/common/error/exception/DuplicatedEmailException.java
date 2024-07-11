package org.store.clothstar.common.error.exception;

import org.store.clothstar.common.error.ErrorCode;

public class DuplicatedEmailException extends RuntimeException {
    private final ErrorCode errorCode;

    public DuplicatedEmailException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
