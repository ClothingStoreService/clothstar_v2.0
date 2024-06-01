package org.store.clothstar.common.dto;

import lombok.Getter;

@Getter
public class ErrorResponseDTO {
    private int errorCode;
    private String message;

    public ErrorResponseDTO(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}