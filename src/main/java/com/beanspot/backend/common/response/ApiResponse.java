package com.beanspot.backend.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.common.exception.ExceptionDto;
import com.beanspot.backend.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * 모든 API 응답을 하나의 포맷으로 통일.
 * httpStatus는 바디에는 숨기고(ResponseBodyAdvice에서 헤더로만 반영)
 */
public record ApiResponse<T>(
        @JsonIgnore HttpStatus httpStatus,
        boolean success,
        @Nullable T data,
        @Nullable ExceptionDto error
) {
    public static <T> ApiResponse<T> ok(@Nullable T data) {
        return new ApiResponse<>(HttpStatus.OK, true, data, null);
    }
    public static <T> ApiResponse<T> created(@Nullable T data) {
        return new ApiResponse<>(HttpStatus.CREATED, true, data, null);
    }
    public static <T> ApiResponse<T> fail(CustomException e) {
        return new ApiResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
        return new ApiResponse<>(errorCode.getHttpStatus(), false, null, ExceptionDto.of(errorCode, message));
    }
}

