package org.store.clothstar.common.util;

import org.store.clothstar.common.dto.MessageDTO;

public class MessageDTOBuilder {
    public static MessageDTO buildMessage(int status, String message) {
        return MessageDTO.builder()
                .statusCode(status)
                .message(message)
                .build();
    }
}