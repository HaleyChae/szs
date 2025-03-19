package com.szs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "Login Response")
public class LoginResponseDto extends ApiResponseDto{
    @Schema(description = "토큰")
    private String accessToken;
}
