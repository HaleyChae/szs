package com.szs.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.dto.ApiResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtExceptionHandler {
    private final ObjectMapper objectMapper;

    public void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        ApiException ae;
        if (e.getClass().equals(ExpiredJwtException.class)) {
            ae = new ApiException(ErrorEnum.TOKEN_EXPIRE);
        } else {
            ae = new ApiException(ErrorEnum.NO_TOKEN);
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ae.getError().getStatusCode().value());
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        ApiResponseDto.builder()
                                .result(false)
                                .message(ae.getError().getMessage())
                                .build()
                )
        );
    }
}
