package org.store.clothstar.productLine.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.store.clothstar.common.exception.ExceptionType;

@RequiredArgsConstructor
public enum ProductLineExceptionType implements ExceptionType {
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 5001, "해당 상품을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final int exceptionCode;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
    }

    public int exceptionCode() {
        return exceptionCode;
    }

    @Override
    public String message() {
        return message;
    }
}
