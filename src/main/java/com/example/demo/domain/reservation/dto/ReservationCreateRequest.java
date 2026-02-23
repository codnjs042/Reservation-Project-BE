package com.example.demo.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @Schema(description="인원 수", example="2")
        int headCount,
        @Schema(description="예약 날짜 시간", example="2026-02-01T09:00:00")
        LocalDateTime targetDateTime
) {
}
