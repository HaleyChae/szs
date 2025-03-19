package com.szs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "회원가입 사용자 정보")
public class SignupRequestDto {
    @Schema(description = "아이디", example = "example1")
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String userId;
    @Schema(description = "비밀번호", example = "example1234")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
    @Schema(description = "이름", example = "채은별")
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;
    @Schema(description = "주민등록번호", example = "950517-1234567")
    @Pattern(regexp = "^(\\d{2})((0[1-9])|(1[0-2]))((0[1-9])|([12][0-9])|3[01])-([1234])\\d{6}$", message = "유효하지 않은 주민등록번호 형식입니다.")
    @NotBlank(message = "주민등록번호를 입력해 주세요.")
    private String regNo;
}
