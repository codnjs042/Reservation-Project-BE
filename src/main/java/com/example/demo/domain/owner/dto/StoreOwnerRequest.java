package com.example.demo.domain.owner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record StoreOwnerRequest(
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
        @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
        int page,
        @Schema(description = "페이지 크기", example = "10")
        @Positive(message = "페이지 크기는 1 이상이어야 합니다.")
        int size
) {
}
