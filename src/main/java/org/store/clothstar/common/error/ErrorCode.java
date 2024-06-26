package org.store.clothstar.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "refresh 토큰이 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh 토큰이 만료되었거나 유효하지 않습니다."),
    ERROR_MAIL_SENDER(HttpStatus.BAD_REQUEST, "메일을 전송하는 도중 에러가 발생하였습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
