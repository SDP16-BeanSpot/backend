package com.beanspot.backend.dto.kakaomap;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoAddressResponse {
    private List<KakaoAddressDTO> documents;
}
