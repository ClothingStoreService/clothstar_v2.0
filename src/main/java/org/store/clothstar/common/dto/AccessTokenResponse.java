package org.store.clothstar.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessTokenResponse {
    String accessToken;
    String message;
    Boolean success;
}