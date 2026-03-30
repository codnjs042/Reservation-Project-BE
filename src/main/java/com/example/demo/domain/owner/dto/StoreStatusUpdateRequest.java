package com.example.demo.domain.owner.dto;

import com.example.demo.domain.store.domain.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record StoreStatusUpdateRequest(
        @Schema(description = "영업 상태", example="OPEN")
        StoreStatus status
) {
}
