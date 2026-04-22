package com.example.demo.global.infra.kakao;

import io.swagger.v3.oas.annotations.media.Schema;

public record PointDto(
        @Schema(description = "위도")
        Double latitude,
        @Schema(description = "경도")
        Double longitude
) {
}
