package com.szs.controller;

import com.szs.dto.ApiResponseDto;
import com.szs.dto.LoginRequestDto;
import com.szs.dto.LoginResponseDto;
import com.szs.dto.SignupRequestDto;
import com.szs.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name="1. Auth", description = "회원가입/로그인")
public class AuthController {
    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/szs/signup")
    public ResponseEntity<ApiResponseDto> signup(
            @RequestBody @Valid SignupRequestDto member) {
        return ResponseEntity.ok(memberService.signUp(member));

    }

    @Operation(summary = "로그인")
    @PostMapping("/szs/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto member) {
        return ResponseEntity.ok(memberService.login(member));
    }
}
