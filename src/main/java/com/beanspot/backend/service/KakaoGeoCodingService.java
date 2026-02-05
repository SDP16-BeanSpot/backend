package com.beanspot.backend.service;

import com.beanspot.backend.dto.kakaomap.KakaoAddressDTO;
import com.beanspot.backend.dto.kakaomap.KakaoAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoGeoCodingService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.kakao.rest-api-key}")
    private String apiKey;

    public GeoPoint convert(String address) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", address)
                .build()
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoAddressResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, KakaoAddressResponse.class);

        if (response.getBody() == null || response.getBody().getDocuments().isEmpty()) {
            throw new IllegalArgumentException("주소를 좌표로 변환할 수 없습니다: " + address);
        }
        KakaoAddressDTO doc = response.getBody().getDocuments().get(0);

        return new GeoPoint(
                Double.parseDouble(doc.getY()),
                Double.parseDouble(doc.getX())
        );
    }
}
