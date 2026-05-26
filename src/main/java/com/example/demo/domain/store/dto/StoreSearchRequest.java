package com.example.demo.domain.store.dto;

import com.example.demo.domain.store.domain.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record StoreSearchRequest(
        @Schema(description = "검색어(가게명/카테고리/주소)", example="호식이")
        @Size(max = 50, message="검색어는 50자 이내로 입력해 주세요.")
        String keyword,
        @Schema(description = "카테고리", example="KOREAN")
        StoreCategory category,
        @Schema(description = "지역코드", example="11")
        String cd,
        @Schema(description = "페이지 번호 (0부터 시작)", example="0")
        @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
        int page,
        @Schema(description = "페이지 크기", example="10")
        @Positive(message = "페이지 크기는 1 이상이어야 합니다.")
        int size
) {
}
