package com.szs.exception;

import com.szs.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleException(Exception ex) {
        log.error("ERROR: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponseDto.builder()
                                .result(false)
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        String firstError = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponseDto.builder()
                                .result(false)
                                .message(firstError)
                                .build()
                );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDto> handleApiException(ApiException ex) {
        return ResponseEntity
                .status(ex.getError().getStatusCode())
                .body(
                        ApiResponseDto.builder()
                                .result(false)
                                .message(ex.getError().getMessage())
                                .build()
                );
    }
}