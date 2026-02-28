package com.beanspot.backend.entity;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;

public enum SocialType {
    KAKAO,
    NAVER,
    GOOGLE,
    NONE;

    public static SocialType from(String input){
        try{
            return SocialType.valueOf(input.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
