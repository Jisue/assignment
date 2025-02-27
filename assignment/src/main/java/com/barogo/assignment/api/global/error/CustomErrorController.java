package com.barogo.assignment.api.global.error;

import com.barogo.assignment.api.global.common.response.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot의 기본 오류 페이지 처리
 */

@Slf4j
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<CommonResponse<Void>> handleError(HttpServletResponse response) {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());

        log.warn("에러 발생: {} {}", status.value(), status.getReasonPhrase());

        return CommonResponse.fail(status, "오류가 발생했습니다.");
    }
}
