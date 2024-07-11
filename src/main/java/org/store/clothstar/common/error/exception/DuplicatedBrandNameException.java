package org.store.clothstar.common.error.exception;

import org.store.clothstar.common.error.ErrorCode;

public class DuplicatedBrandNameException extends RuntimeException {
    private final ErrorCode errorCode;

    public DuplicatedBrandNameException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
