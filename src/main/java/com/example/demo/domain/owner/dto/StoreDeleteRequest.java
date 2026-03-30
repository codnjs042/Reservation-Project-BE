package com.example.demo.domain.owner.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record StoreDeleteRequest(
        @Schema(description = "삭제 대상 ID 목록", example="[1, 2]")
        List<Long> ids
) {
}
