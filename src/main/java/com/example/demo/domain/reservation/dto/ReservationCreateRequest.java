package com.example.demo.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @Schema(description="예약자명", example="김둘리")
        String name,
        @Schema(description="인원 수", example="2")
        int headCount,
        @Schema(description="예약 날짜 시간", example="2026-02-01T09:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime targetDateTime
) {
}
