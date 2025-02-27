package com.barogo.assignment.api.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {
    private String accessToken;
    private String refreshToken;
}
