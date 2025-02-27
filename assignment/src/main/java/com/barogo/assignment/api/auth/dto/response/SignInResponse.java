package com.barogo.assignment.api.auth.dto.response;

import com.barogo.assignment.api.auth.dto.Token;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResponse {
    private Token token;
}
