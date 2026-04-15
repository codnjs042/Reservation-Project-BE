package com.example.demo.domain.owner.dto;

import com.example.demo.domain.store.domain.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StoreStatusUpdateRequest(
        @Schema(description = "영업 상태", example="OPEN")
        @NotNull(message = "영업 상태를 선택해 주세요.")
        StoreStatus status
) {
}
