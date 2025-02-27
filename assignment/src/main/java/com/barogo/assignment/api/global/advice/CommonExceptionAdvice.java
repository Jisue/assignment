package com.barogo.assignment.api.global.advice;

import com.barogo.assignment.api.global.common.response.CommonResponse;
import com.barogo.assignment.api.global.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CommonExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception ex) {
        log.error("예외 발생: {}", ex.getMessage(), ex);
        return CommonResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // 커스텀 Exception
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonResponse<Void>> handleCommonException(CommonException ex) {
        log.warn("CommonException 예외 메시지: {}", ex.getMessage());
        return CommonResponse.fail(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 첫 번째 오류 메시지만 가져오기
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        errors.put("message", errorMessage);

        return CommonResponse.fail(HttpStatus.BAD_REQUEST, errorMessage);
    }

    // 날짜 변환 오류 처리
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, DateTimeParseException.class})
    public ResponseEntity<CommonResponse<Void>> handleDateTimeParseException(Exception ex) {
        log.warn("예외 발생: {}", ex.getMessage());
        return CommonResponse.fail(HttpStatus.BAD_REQUEST, "날짜 형식이 올바르지 않습니다." +
                "(yyyy-MM-dd, 현재 날짜보다 이후 날짜를 입력해주세요.)");
    }

    // 잘못된 URL 요청 (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleNotFound(NoHandlerFoundException ex) {
        log.warn("잘못된 URL 요청: {}", ex.getRequestURL());
        return CommonResponse.fail(HttpStatus.NOT_FOUND, "요청하신 URL을 찾을 수 없습니다.");
    }

    // 400 오류 (잘못된 요청)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleBadRequest(IllegalArgumentException ex) {
        log.warn("잘못된 요청: {}", ex.getMessage());
        return CommonResponse.fail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 403 오류 (권한 없음)
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<CommonResponse<Void>> handleForbidden(HttpClientErrorException.Forbidden ex) {
        log.warn("권한 없음: {}", ex.getMessage());
        return CommonResponse.fail(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }

    // @Valid 검증 실패 시 (예: DTO 검증 실패)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationException(ConstraintViolationException ex) {
        log.warn("요청 데이터 검증 실패: {}", ex.getMessage());
        return CommonResponse.fail(HttpStatus.BAD_REQUEST, "입력 데이터가 유효하지 않습니다.");
    }
}
