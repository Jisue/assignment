package com.barogo.assignment.api.auth.service;

import com.barogo.assignment.api.auth.dto.Token;
import com.barogo.assignment.api.auth.dto.request.SignInRequest;
import com.barogo.assignment.api.auth.dto.request.SignUpRequest;
import com.barogo.assignment.api.auth.dto.response.SignInResponse;
import com.barogo.assignment.api.auth.dto.response.SignUpResponse;
import com.barogo.assignment.api.global.exception.CommonException;
import com.barogo.assignment.api.global.security.jwt.JwtToken;
import com.barogo.assignment.api.global.security.jwt.JwtTokenProvider;
import com.barogo.assignment.api.user.domain.entity.User;
import com.barogo.assignment.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {
    
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        User user = User.builder()
                .userId(signUpRequest.getId())
                .password(passwordEncoder.encode(signUpRequest.getPassword())) // 비밀번호 암호화
                .name(signUpRequest.getName())
                .type(signUpRequest.getType())
                .createDate(LocalDateTime.now())
                .build();

        User createdUser = userService.createUser(user);
        log.debug("Created userId: {}", createdUser.getUserId());

        Authentication authentication = jwtTokenProvider.getAuthentication(user);
        Token token = getJwtToken(authentication);

        return SignUpResponse.builder()
                .token(token)
                .build();
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getId(), signInRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Token token = getJwtToken(authentication);

        return SignInResponse.builder()
                .token(token)
                .build();
    }

    private Token getJwtToken(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new CommonException("잘못된 권한 접근", HttpStatus.FORBIDDEN);
        }

        JwtToken jwtToken = jwtTokenProvider.createJwtToken(authentication);

        // TODO: 생성 시점에 redis에 (userId, refresh token) 저장 후, 추후 token 재발급 요청시 매칭하여 확인 후 재발급.

        return Token.builder()
                .accessToken(jwtToken.getAccessTokenInfo().getAccessToken())
                .refreshToken(jwtToken.getRefreshTokenInfo().getRefreshToken())
                .build();
    }

}
