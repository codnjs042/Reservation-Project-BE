package com.example.demo.domain.storeTable.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record StoreTableBulkUpdateRequest(
        @Schema(description = "기존 테이블 타입", example="2인석")
        String oldTableName,
        @Schema(description = "변경 테이블 타입", example="4인석")
        String newTableName,
        @Schema(description = "최소 수용 인원", example="2")
        int minCapacity,
        @Schema(description = "최대 수용 인원", example="4")
        int maxCapacity,
        @Schema(description = "테이블 수량", example="2")
        int count
) {
}
