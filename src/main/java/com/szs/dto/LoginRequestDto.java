package com.szs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "로그인 계정")
public class LoginRequestDto {
    @Schema(description = "아이디", example = "example1")
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String userId;
    @Schema(description = "비밀번호", example = "example1234")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
}
