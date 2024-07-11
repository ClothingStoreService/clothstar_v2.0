package org.store.clothstar.common.error.exception;

import org.store.clothstar.common.error.ErrorCode;

public class DuplicatedBizNoException extends RuntimeException {
    private final ErrorCode errorCode;

    public DuplicatedBizNoException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
