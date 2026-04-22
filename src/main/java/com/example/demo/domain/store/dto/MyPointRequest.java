package com.example.demo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyPointRequest(
        @Schema(description = "위도", example= "36.8")
        Double latitude,
        @Schema(description = "경도", example= "127.15")
        Double longitude
) {
}
