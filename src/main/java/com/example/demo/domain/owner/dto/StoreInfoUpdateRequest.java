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
        @Schema(description = "주소", example="천안시 동남구 신부 1길 2")
        @NotBlank(message = "주소를 입력해 주세요.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s&()\\-,.]{1,100}$", message = "주소는 1자 이상 100자 이하의 숫자/영문/한글/공백/특수문자(&()-,.)로 입력해 주세요.")
        String address,
        @Schema(description = "상세주소", example="101호")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s&()\\-,.]{0,50}$", message = "상세 주소는 50자 이하의 숫자/영문/한글/공백/특수문자(&()-,.)로 입력해 주세요.")
        String detailAddress,
        @Schema(description = "우편번호", example="31124")
        @NotBlank(message = "우편번호를 입력해 주세요.")
        @Pattern(regexp = "^[0-9]{5}$", message = "우편번호는 5자리 숫자로 입력해 주세요.")
        String zipCode,
        @Schema(description = "시군구코드", example="44131")
        @NotBlank(message = "시군구코드를 입력해 주세요.")
        @Pattern(regexp = "^[0-9]{5}$", message = "시군구코드는 5자리 숫자로 입력해 주세요.")
        String sigunguCode,
        @Schema(description = "전화번호", example="04182829999")
        @NotBlank(message = "전화번호를 입력해 주세요.")
        @Pattern(regexp = "^[0-9]{8,11}$", message = "전화번호는 8자 이상 11자 이하의 숫자로 입력해 주세요.")
        String phone
) {
}
