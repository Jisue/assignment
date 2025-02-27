package com.barogo.assignment.api.auth.controller;

import com.barogo.assignment.api.auth.dto.request.SignInRequest;
import com.barogo.assignment.api.auth.dto.request.SignUpRequest;
import com.barogo.assignment.api.auth.dto.response.SignInResponse;
import com.barogo.assignment.api.auth.dto.response.SignUpResponse;
import com.barogo.assignment.api.auth.service.AuthService;
import com.barogo.assignment.api.global.common.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-up", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return CommonResponse.success(authService.signUp(signUpRequest));
    }

    @PostMapping(value = "/sign-in", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponse<SignInResponse>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return CommonResponse.success(authService.signIn(signInRequest));
    }

}
