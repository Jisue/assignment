package com.barogo.assignment.api.global.common.response;

import com.barogo.assignment.api.global.common.code.CommonResponseCode;
import com.barogo.assignment.api.global.exception.CommonException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String message;
    private T data;

    // 성공 응답 생성
    public static <T> ResponseEntity<CommonResponse<T>> success(T data) {
        return ResponseEntity.ok(new CommonResponse<>(CommonResponseCode.SUCCESS.getCode(), CommonResponseCode.SUCCESS.getMessage(), data));
    }

    // 실패 응답 생성
    public static ResponseEntity<CommonResponse<Void>> fail(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new CommonResponse<>(status.value(), message, null));
    }

    // 실패 응답 생성
    public static ResponseEntity<CommonResponse<Void>> fail(CommonException ex) {
        // 기본적으로 code가 설정되지 않았다면(0이라면) 응답으로 HttpStatus 사용
        int code = ex.getCode() == 0 ? ex.getHttpStatus().value() : ex.getCode();
        return ResponseEntity.status(ex.getHttpStatus()).body(new CommonResponse<>(code, ex.getMessage(), null));
    }
}
