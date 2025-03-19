package com.szs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.dto.ApiResponseDto;
import com.szs.exception.ApiException;
import com.szs.exception.ErrorEnum;
import com.szs.exception.JwtExceptionHandler;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtExceptionHandler jwtExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);

        try {
            if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
                String token = bearerToken.substring(TOKEN_PREFIX.length());

                jwtTokenProvider.checkToken(token);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(jwtTokenProvider.getUserIdFromToken(token), null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch(Exception e) {
            jwtExceptionHandler.exceptionHandler(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
