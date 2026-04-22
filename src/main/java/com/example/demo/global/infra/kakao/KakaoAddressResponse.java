package com.example.demo.global.infra.kakao;

import java.util.List;

public record KakaoAddressResponse(
        List<Document> documents
) {
    public record Document(
            String x,
            String y
    ){}
}
