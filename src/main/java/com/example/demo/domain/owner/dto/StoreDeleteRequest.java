package com.example.demo.domain.owner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record StoreDeleteRequest(
        @Schema(description = "삭제 대상 ID 목록", example="[1, 2]")
        @NotEmpty(message="삭제 대상을 1개 이상 선택해 주세요.")
        @Size(min=1, max=100, message = "최대 100개 삭제 가능합니다.")
        List<Long> ids
) {
}
