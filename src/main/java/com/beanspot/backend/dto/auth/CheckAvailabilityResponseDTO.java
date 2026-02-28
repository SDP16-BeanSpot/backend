package com.beanspot.backend.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckAvailabilityResponseDTO {
    private final boolean available; // 닉네임 사용 가능 여부
    private final String value;   // 확인한 닉네임, username 등
    private final String type;
    private final String message;
}
