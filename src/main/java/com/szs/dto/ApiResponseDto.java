package com.szs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "API 공통 Response")
public class ApiResponseDto {
    @Schema(description = "성공여부")
    private boolean result;
    @Schema(description = "메세지")
    private String message;
}
