package com.barogo.assignment.api.global.exception;

import com.barogo.assignment.api.global.common.code.CommonResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonException extends RuntimeException {

    private int code;
    private final String message;
    private final HttpStatus httpStatus;

    public CommonException() {
        this.code = CommonResponseCode.FAILED.getCode();
        this.message =  CommonResponseCode.FAILED.getMessage();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CommonException(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public CommonException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
