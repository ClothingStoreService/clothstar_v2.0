package org.store.clothstar.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ModifyNameRequest {
    @NotNull
    private String name;
}