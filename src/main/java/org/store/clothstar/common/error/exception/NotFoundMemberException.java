package org.store.clothstar.common.error.exception;

import org.store.clothstar.common.error.ErrorCode;

public class NotFoundMemberException extends RuntimeException {
    private final ErrorCode errorCode;

    public NotFoundMemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
