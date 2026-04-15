package com.example.demo.domain.admin.dto;

import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record StoreAdminRequest(
        @Schema(description = "검색어(가게번호/가게명/사업자번호)", example="호식이국밥")
        @Size(max=50, message= "검색어는 50자 이내로 입력해 주세요.")
        String keyword,
        @Schema(description = "카테고리", example="KOREAN")
        StoreCategory category,
        @Schema(description = "영업상태", example="OPEN")
        StoreStatus status
) {
}
