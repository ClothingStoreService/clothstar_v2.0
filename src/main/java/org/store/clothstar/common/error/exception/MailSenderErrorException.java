package org.store.clothstar.common.error.exception;

import org.store.clothstar.common.error.ErrorCode;

public class MailSenderErrorException extends RuntimeException {
    private final ErrorCode errorCode;

    public MailSenderErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
