package com.example.demo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record StoreSearchRequest(
        @Schema(description = "검색어(가게명/카테고리/주소)", example="호식이")
        String keyword
) {
}
