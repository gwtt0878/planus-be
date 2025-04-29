package com.planus.common.exception;

import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${client.host}")
    private String clientHost;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(400, e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        log.error("NoSuchElementException", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(404, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(400, e.getMessage()));
    }

    @ExceptionHandler(NeedNicknameException.class)
    public ResponseEntity<Map<String, String>> handleNeedNicknameException(NeedNicknameException e) {
        log.error("NeedNicknameException", e);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(clientHost + "/register/google?email=" + e.getMessage()));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(500, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(500, e.getMessage()));
    }
}
