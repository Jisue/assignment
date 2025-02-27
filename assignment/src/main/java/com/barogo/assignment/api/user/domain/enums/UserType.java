package com.barogo.assignment.api.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static java.lang.String.format;

@Getter
@AllArgsConstructor
public enum UserType {
    ORDER("ORDER", "주문 고객"),
    DELIVERY("DELIVERY", "배달 고객")
    ;

    private final String code;

    private final String description;

    public static UserType ofCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(format("user type not exists: %s (code)", code)));
    }

    @JsonCreator
    public static UserType from(String code) {
        return ofCode(code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
