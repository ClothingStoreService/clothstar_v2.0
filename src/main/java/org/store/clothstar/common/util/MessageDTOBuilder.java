package org.store.clothstar.common.util;

import org.store.clothstar.common.dto.MessageDTO;

public class MessageDTOBuilder {

    public static MessageDTO buildMessage(Long id, int status, String message) {
        return MessageDTO.builder()
                .id(id)
                .status(status)
                .message(message)
                .build();
    }

    public static MessageDTO buildMessage(int status, String message) {
        return MessageDTO.builder()
                .status(status)
                .message(message)
                .build();
    }

    public static MessageDTO buildMessage(int status, String message, boolean success) {
        return MessageDTO.builder()
                .status(status)
                .message(message)
                .success(success)
                .build();
    }
}