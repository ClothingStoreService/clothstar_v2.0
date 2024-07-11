package org.store.clothstar.common.error.exception;

import org.store.clothstar.common.error.ErrorCode;

public class DuplicatedSellerException extends RuntimeException {
    private final ErrorCode errorCode;

    public DuplicatedSellerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
