package com.example.demo.domain.storeTable.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record StoreTableRegisterRequest(
        @Schema(description = "테이블 타입", example="4인석")
        String tableName,
        @Schema(description = "최소 수용 인원", example="2")
        int minCapacity,
        @Schema(description = "최대 수용 인원", example="4")
        int maxCapacity,
        @Schema(description = "해당 타입 테이블 개수", example="2")
        int count
) {}
