package org.store.clothstar.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
    HttpStatus status();

    String message();
}
