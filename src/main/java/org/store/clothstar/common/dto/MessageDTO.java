package org.store.clothstar.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MessageDTO {
    private int statusCode;
    private String message;

    public MessageDTO(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}