package com.barogo.assignment.api.global.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponseCode implements CommonCode {

    SUCCESS(0, "성공"),
    FAILED(1, "실패");

    private final int code;
    private final String message;
}

