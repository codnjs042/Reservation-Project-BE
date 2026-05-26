package com.example.demo.domain.reservation.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.List;

public record ReservationSearchRequest(
        @Schema(description="예약 조회 시작 날짜", example = "2026-02-25")
        LocalDate startDate,
        @Schema(description="예약 조회 종료 날짜", example = "2026-02-25")
        LocalDate endDate,
        @Schema(description="예약 상태", example = "CONFIRMED")
        List<ReservationStatus> status,
        @Schema(description = "페이지 번호 (0부터 시작)", example="0")
        @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
        int page,
        @Schema(description = "페이지 크기", example="20")
        @Positive(message = "페이지 크기는 1 이상이어야 합니다.")
        int size
) {
}
