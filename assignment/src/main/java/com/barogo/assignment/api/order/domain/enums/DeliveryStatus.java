package com.barogo.assignment.api.order.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static java.lang.String.format;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {
    PENDING("PENDING", "배송대기"),
    STARTED("STARTED", "배송시작"),
    COMPLETED("COMPLETED", "배송완료")
    ;

    private final String code;

    private final String description;

    public static DeliveryStatus ofCode(String code) {
        return Arrays.stream(values())
                .filter(r -> r.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(format("delivery status type not exists: %s (code)", code)));
    }

    @JsonCreator
    public static DeliveryStatus from(String code) {
        return ofCode(code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
