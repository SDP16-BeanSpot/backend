package com.beanspot.backend.common.exception;

import org.springframework.http.HttpStatus;

/** 에러 코드(도메인별 범위는 나중에 확장) */
public enum ErrorCode {
    // ==================== Common (10xxx) ====================
    INVALID_INPUT_VALUE(10001, HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),

    CALENDAR_SCHEDULE_NOT_FOUND(11001, HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    CALENDAR_INVALID_REPEAT_RULE(11002, HttpStatus.BAD_REQUEST, "유효하지 않은 반복 주기입니다."),

    // ==================== Auth (21xxx) ====================
    AUTH_LOGIN_REQUIRED(21001, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    AUTH_INVALID_CREDENTIALS(21002, HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    AUTH_EMAIL_NOT_FOUND(21003, HttpStatus.UNAUTHORIZED, "이메일이 존재하지 않습니다."),
    AUTH_TOKEN_EXPIRED(21004, HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다."),
    AUTH_TOKEN_INVALID(21005, HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),
    AUTH_ACCESS_DENIED(21006, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    AUTH_SOCIAL_ID_NOT_FOUND(21007, HttpStatus.UNAUTHORIZED, "해당 소셜아이디로 가입된 정보가 없습니다.."),
    AUTH_ACCOUNT_LOCKED(21008, HttpStatus.FORBIDDEN, "계정이 잠겼습니다. 관리자에게 문의하세요."),

    AUTH_USERID_ALREADY_EXISTS(21101, HttpStatus.CONFLICT, "이미 사용 중인 사용자 아이디입니다."),
    AUTH_EMAIL_ALREADY_EXISTS(21102, HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    AUTH_NICKNAME_ALREADY_EXISTS(21103, HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    AUTH_PASSWORD_WEAK(21104, HttpStatus.BAD_REQUEST, "비밀번호가 보안 규칙에 맞지 않습니다."),
    AUTH_PASSWORD_MISMATCH(21105, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    AUTH_SOCIAL_ID_ALREADY_EXISTS(21106, HttpStatus.CONFLICT, "이미 사용 중인 소셜 아이디입니다."),

    AUTH_EMAIL_NOT_VERIFIED(21201, HttpStatus.FORBIDDEN, "이메일 인증이 완료되지 않았습니다."),
    AUTH_VERIFICATION_CODE_INVALID(21202, HttpStatus.BAD_REQUEST, "인증 코드가 유효하지 않습니다."),
    AUTH_VERIFICATION_CODE_EXPIRED(21203, HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),


    INTERNAL_SERVER_ERROR(90000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    ;


    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getCode() { return code; }
    public HttpStatus getHttpStatus() { return httpStatus; }
    public String getMessage() { return message; }
}

