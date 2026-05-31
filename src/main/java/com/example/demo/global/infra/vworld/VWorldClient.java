package com.example.demo.global.infra.vworld;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VWorldClient {
    @Value("${vworld.api.key}")
    private String apiKey;

    @Value("${app.backend-url}")
    private String domain;

    private final RestTemplate restTemplate;

    public List<AreaResponse> getArea(String cd){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://api.vworld.kr/req/data")
                .queryParam("request", "GetFeature")
                .queryParam("key", apiKey)
                .queryParam("geometry", "false")
                .queryParam("domain", domain);

        if(cd==null || cd.isBlank()){
            URI uri = builder.queryParam("size", "17")
                    .queryParam("data", "LT_C_ADSIDO_INFO")
                    .queryParam("attrFilter", "ctprvn_cd:like:%")
                    .build()
                    .toUri();

            return fetch(uri, new ParameterizedTypeReference<VWorldAreaResponse<Region>>(){});
        }

        else{
            URI uri = builder
                    .queryParam("size", "50")
                    .queryParam("data", "LT_C_ADSIGG_INFO")
                    .queryParam("attrFilter", "sig_cd:like:"+cd+"%")
                    .build()
                    .toUri();

            return fetch(uri, new ParameterizedTypeReference<VWorldAreaResponse<District>>(){});
        }
    }

    private <T> List<AreaResponse> fetch(URI uri, ParameterizedTypeReference<VWorldAreaResponse<T>> type){
        try {
            ResponseEntity<VWorldAreaResponse<T>> response = restTemplate.exchange(
                    uri, HttpMethod.GET, null, type
            );

            log.info("[V-WORLD API Response] 응답 상태코드: {}", response.getStatusCode());
            log.info("[V-WORLD API Response] 응답 결과: {}", response.getBody());

            if (response.getBody() != null && response.getBody().response().result() != null) {
                return response.getBody().response().result().featureCollection().features().stream()
                        .map(feature -> {
                            T prop = feature.properties();
                            if(prop instanceof Region r){
                                return new AreaResponse(r.ctprvn_cd(), r.ctp_kor_nm());
                            }
                            else if(prop instanceof District d){
                                return new AreaResponse(d.sig_cd(), d.sig_kor_nm());
                            }
                            else
                                return null;
                        })
                        .filter(java.util.Objects::nonNull)
                        .toList();
            } else {
                log.warn("[V-WORLD API Empty] 검색 결과가 없습니다.");
            }
        } catch (Exception e) {
            log.error("[V-WORLD API error] API 호출 중 오류 발생: {}", e.getMessage());
        }
        return List.of();
    }
}