package org.store.clothstar.common.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidErrorResponseDTO {
    private int errorCode;
    private Map<String, String> errorMap;

    public ValidErrorResponseDTO(int errorCode, Map<String, String> errorMap) {
        this.errorCode = errorCode;
        this.errorMap = errorMap;
    }
}
