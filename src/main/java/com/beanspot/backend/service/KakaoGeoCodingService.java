package com.beanspot.backend.service;

import com.beanspot.backend.dto.kakaomap.KakaoAddressDTO;
import com.beanspot.backend.dto.kakaomap.KakaoAddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoGeoCodingService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.kakao.rest-api-key}")
    private String apiKey;

    public GeoPoint convert(String address) {
        // 주소 데이터가 비어있는 경우 사전 방어
        if (address == null || address.isBlank()) {
            return null;
//            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        String url = UriComponentsBuilder
                .fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", address)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoAddressResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, KakaoAddressResponse.class);

            // API는 성공했으나 검색 결과가 없는 경우
            if (response.getBody() == null || response.getBody().getDocuments().isEmpty()) {
                log.info("[KakaoGeoCoding] 검색 결과 없음: {}", address);
                return null;
                //                throw new CustomException(ErrorCode.INVALID_LOCATION_FORMAT);
            }

            KakaoAddressDTO doc = response.getBody().getDocuments().get(0);

            return new GeoPoint(
                    Double.parseDouble(doc.getY()),
                    Double.parseDouble(doc.getX())
            );

        } catch (Exception e) {
            log.error("[KakaoGeoCoding] 주소 변환 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }
}
