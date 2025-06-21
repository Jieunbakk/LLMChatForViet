package com.llm.llm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 상태 코드
                .body(ex.getMessage());       // 예외 메시지 반환
    }


    @ExceptionHandler(CloneNotSupportedException.class)
    public ResponseEntity<String> handleCloneNotSupportedException(CloneNotSupportedException ex) {
        return ResponseEntity
                .status(409)
                .body(ex.getMessage());       // 예외 메시지 반환
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 상태 코드 반환
                .body(ex.getMessage()); // 예외 메시지 반환
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<String> handleUnauthorized(HttpClientErrorException.Unauthorized ex) {
        return ResponseEntity
                .status(403)
                .body(ex.getMessage());
    }
}
