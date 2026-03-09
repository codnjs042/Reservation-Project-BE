package com.example.demo.domain.store.dto;

import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record StoreUpdateRequest(
        @Schema(description = "가게명", example="둘리파스타")
        String name,
        @Schema(description = "카테고리", example="WESTERN")
        StoreCategory category,
        @Schema(description = "주소", example="충청남도 천안시 동남구 신부 1길 2")
        String address,
        @Schema(description = "전화번호", example="04182829999")
        String phone,
        @Schema(description = "예약 간격(단위: 분)", example="120")
        int slotInterval,
        @Schema(description = "이용 시간(단위: 분)", example="90")
        int usageTime,
        @Schema(description = "영업 상태", example="HIDDEN")
        StoreStatus status
) {
}
