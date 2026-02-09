package com.example.demo.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReservationTimeSlotRequest(
        @Schema(description="인원 수", example="2")
        int headCount,
        @Schema(description="예약 날짜", example="2026-02-01")
        LocalDate targetDate
) {}
