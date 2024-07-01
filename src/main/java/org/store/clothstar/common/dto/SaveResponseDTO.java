package org.store.clothstar.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveResponseDTO {
    private Long id;
    private int statusCode;
    private String message;
}
