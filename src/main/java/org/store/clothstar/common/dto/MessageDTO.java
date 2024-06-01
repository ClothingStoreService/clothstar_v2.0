package org.store.clothstar.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MessageDTO {
    private Long id;
    private int statusCode;
    private String message;

    public MessageDTO(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}