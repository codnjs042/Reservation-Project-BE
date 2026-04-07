package com.example.demo.domain.admin.dto;

import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record StoreAdminRequest(
        @Schema(description = "검색어(가게번호/가게명/사업자번호)", example="호식이국밥")
        String keyword,
        @Schema(description = "카테고리", example="KOREAN")
        StoreCategory category,
        @Schema(description = "영업상태", example="OPEN")
        StoreStatus status
) {
}
