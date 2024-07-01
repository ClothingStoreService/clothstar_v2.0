package org.store.clothstar.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
    HttpStatus status();

    int exceptionCode();

    String message();
}
