package com.barogo.assignment.api.auth.dto.request;

import com.barogo.assignment.api.user.domain.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotBlank(message = "id는 필수입니다.")
    private String id;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[\\W_]).{12,}$",
            message = "비밀번호는 대문자, 소문자, 숫자 또는 특수문자 중 3가지 이상을 포함해야 하며 최소 12자 이상이어야 합니다.")
    private String password;

    @NotNull(message = "타입은 필수입니다.")
    private UserType type;
}
