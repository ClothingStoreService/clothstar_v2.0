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
    private int status;
    private String message;
    private String redirectURI;
    private boolean success;

    public MessageDTO(int status, String message, String redirectURI) {
        this.status = status;
        this.message = message;
        this.redirectURI = redirectURI;
    }
}