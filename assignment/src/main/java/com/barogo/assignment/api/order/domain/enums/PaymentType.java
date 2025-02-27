package com.barogo.assignment.api.order.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static java.lang.String.format;

@Getter
@AllArgsConstructor
public enum PaymentType {
    CARD("CARD", "카드"),
    CASH("CASH", "현금")
    ;

    private final String code;

    private final String description;

    public static PaymentType ofCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(format("payment type not exists: %s (code)", code)));
    }

    @JsonCreator
    public static PaymentType from(String code) {
        return ofCode(code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
