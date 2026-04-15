package com.example.demo.domain.storeTable.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record StoreTableBulkUpdateRequest(
        @Schema(description = "기존 테이블 타입", example="2인석")
        String oldTableName,
        @Schema(description = "변경 테이블 타입", example="4인석")
        @NotBlank(message = "테이블 이름을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]{2,15}$", message = "테이블 이름은 2자 이상 15자 이하의 숫자/영문/한글로 입력해 주세요.")
        String newTableName,
        @Schema(description = "최소 수용 인원", example="2")
        @NotNull(message = "최소 수용 인원을 입력해 주세요.")
        @Positive(message = "최소 수용 인원은 1명 이상입니다.")
        int minCapacity,
        @Schema(description = "최대 수용 인원", example="4")
        @NotNull(message = "최대 수용 인원을 입력해 주세요.")
        @Positive(message = "최대 수용 인원은 1명 이상입니다.")
        int maxCapacity,
        @Schema(description = "테이블 수량", example="2")
        @NotNull(message = "테이블 수량을 입력해 주세요.")
        @Min(value = 0, message = "테이블 수량은 0개 이상입니다.")
        int count
) {
}
