package org.store.clothstar.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "refresh 토큰이 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh 토큰이 만료되었거나 유효하지 않습니다."),
    INVALID_AUTH_CERTIFY_NUM(HttpStatus.BAD_REQUEST, "인증번호가 잘못 되었습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
