package com.example.demo.domain.reservation.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ReservationSearchRequest(
        @Schema(description="예약 조회 날짜", example="2026-02-01")
        LocalDate targetDate,
        @Schema(description="예약 상태", example="")
        List<ReservationStatus> status
) {
}
