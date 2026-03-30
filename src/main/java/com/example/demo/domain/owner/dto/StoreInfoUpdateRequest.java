package com.example.demo.domain.owner.dto;

import com.example.demo.domain.store.domain.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record StoreInfoUpdateRequest(
        @Schema(description = "가게명", example="둘리파스타")
        String name,
        @Schema(description = "카테고리", example="WESTERN")
        StoreCategory category,
        @Schema(description = "전화번호", example="04182829999")
        String phone
) {
}
