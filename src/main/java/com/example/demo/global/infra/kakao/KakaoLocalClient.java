package com.example.demo.global.infra.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLocalClient {
    @Value("${kakao.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public PointDto getCoordinates(String address) {
        URI uri = UriComponentsBuilder.fromUriString("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", address)
                .build()
                .encode()
                .toUri();

        log.info("[KakaoAPI Request] 주소: {}, 요청 URI: {}", address, uri);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, KakaoAddressResponse.class);

            log.info("[KakaoAPI Response] 응답 상태코드: {}", response.getStatusCode());

            if (response.getBody() != null && !response.getBody().documents().isEmpty()) {
                var document = response.getBody().documents().getFirst();
                log.info("[KakaoAPI Success] 위도(y): {}, 경도(x): {}", document.y(), document.x());

                return new PointDto(
                        Double.parseDouble(document.y()),
                        Double.parseDouble(document.x())
                );
            } else {
                log.warn("[KakaoAPI Empty] 검색 결과가 없습니다. 주소: {}", address);
            }
        } catch (Exception e) {
            log.error("[KakaoAPI Error] API 호출 중 오류 발생: {}", e.getMessage());
        }
        return null;
    }
}