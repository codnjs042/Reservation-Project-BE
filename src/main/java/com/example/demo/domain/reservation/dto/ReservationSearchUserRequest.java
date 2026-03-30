package com.example.demo.domain.reservation.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ReservationSearchUserRequest(
        @Schema(description="예약 조회 시작 날짜", example = "2026-02-25")
        LocalDate startDate,
        @Schema(description="예약 조회 종료 날짜", example = "2026-02-25")
        LocalDate endDate,
        @Schema(description="예약 상태", example = "CONFIRMED")
        List<ReservationStatus> status
) {
}
