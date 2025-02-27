package com.barogo.assignment.api.global.security.jwt;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class JwtToken {
    AccessTokenInfo accessTokenInfo;
    RefreshTokenInfo refreshTokenInfo;

    /**
     * Role
     */
    AuthorityRoleType authorityRoleType;

    @Getter
    @Builder
    public static class AccessTokenInfo {
        String accessToken;
        Instant accessTokenIssue;
        Instant accessTokenExpire;
    }

    @Getter
    @Builder
    public static class RefreshTokenInfo {
        String refreshToken;
        Instant refreshTokenIssue;
        Instant refreshTokenExpire;
    }
}
