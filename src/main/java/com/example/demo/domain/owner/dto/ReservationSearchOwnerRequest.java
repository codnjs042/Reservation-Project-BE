package com.example.demo.domain.owner.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ReservationSearchOwnerRequest(
        @Schema(description="검색 타입", example = "name")
        String type,
        @Schema(description="검색어(예약번호 or 예약자명)", example="김둘리")
        String keyword,
        @Schema(description="예약 조회 시작 날짜", example = "2026-03-01")
        LocalDate startDate,
        @Schema(description="예약 조회 종료 날짜", example = "2026-03-31")
        LocalDate endDate,
        @Schema(description="예약 상태", example = "CONFIRMED")
        List<ReservationStatus> status
){
}
