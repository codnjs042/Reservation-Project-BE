package com.example.demo.domain.owner.dto;

import com.example.demo.domain.store.domain.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record StoreCreateRequest(
        @Schema(description = "가게명", example="호식이국밥")
        String name,
        @Schema(description = "카테고리", example="KOREAN")
        StoreCategory category,
        @Schema(description = "주소", example="충청남도 천안시 동남구 신부 1길 1")
        String address,
        @Schema(description = "전화번호", example="04182829898")
        String phone,
        @Schema(description = "사업자명", example="김둘리")
        String ownerName,
        @Schema(description = "사업자등록번호", example="0123456789")
        String businessNumber
) {
}
