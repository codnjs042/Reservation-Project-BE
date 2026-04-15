package com.example.demo.domain.owner.dto;

import com.example.demo.domain.store.domain.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record StoreInfoUpdateRequest(
        @Schema(description = "가게명", example="둘리파스타")
        @NotBlank(message = "가게명을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s&()\\-,.]{1,50}$", message = "가게명은 1자 이상 50자 이하의 숫자/영문/한글/공백/특수문자(&()-,.)로 입력해 주세요.")
        String name,
        @Schema(description = "카테고리", example="WESTERN")
        @NotNull(message = "카테고리를 선택해 주세요.")
        StoreCategory category,
        @Schema(description = "전화번호", example="04182829999")
        @NotBlank(message = "전화번호를 입력해 주세요.")
        @Pattern(regexp = "^[0-9]{8,11}$", message = "전화번호는 8자 이상 11자 이하의 숫자로 입력해 주세요.")
        String phone
) {
}
