package com.szs.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorEnum {
    NO_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 후 사용해 주세요."),
    TOKEN_EXPIRE(HttpStatus.UNAUTHORIZED, "로그인 시간이 만료됐습니다. 다시 로그인해주세요."),
    UNKNOWN_USER(HttpStatus.UNAUTHORIZED, "사용자 정보 조회에 실패했습니다."),

    UNSUPPORTED_VERSION(HttpStatus.INTERNAL_SERVER_ERROR, "버전 설정이 잘못되어 있습니다. 관리자에 문의하세요."),
    SCRAP_API_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "스크랩에 실패했습니다. (스크랩 API 호출 실패)"),
    SCRAP_API_RESPONSE_MAPPING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "스크랩에 실패했습니다. (스크랩 API 응답 매핑 실패)"),

    USER_NOT_ELIGIBLE(HttpStatus.OK, "가입할 수 없는 사용자 입니다."),
    ALREADY_REGISTERED(HttpStatus.OK, "이미 가입 된 회원입니다."),
    DUPLICATE_USERID(HttpStatus.OK, "이미 사용중인 아이디 입니다."),
    INVALID_ACCOUNT(HttpStatus.OK, "아이디 혹은 비밀번호가 틀렸습니다."),
    SCRAP_API_RESULT_FAILED(HttpStatus.OK, "스크랩에 실패했습니다. (스크랩 API 응답 실패)"),
    NO_DATA_TO_SCRAP(HttpStatus.OK, "스크랩 할 데이터가 없습니다."),
    MISSING_DATA_FOR_CALCULATION(HttpStatus.OK, "결정세액 계산에 필요한 데이터가 없습니다. 스크랩 후 다시 조회해주세요.");

    private final HttpStatus statusCode;
    private final String message;
}
