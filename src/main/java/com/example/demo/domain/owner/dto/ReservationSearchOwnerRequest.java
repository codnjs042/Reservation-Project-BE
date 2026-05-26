package com.example.demo.domain.owner.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ReservationSearchOwnerRequest(
        @Schema(description="검색 타입", example = "name")
        String type,
        @Schema(description="검색어(예약번호 or 예약자명)", example="김둘리")
        @Size(max=50, message= "검색어는 50자 이내로 입력해 주세요.")
        String keyword,
        @Schema(description="예약 조회 시작 날짜", example = "2026-03-01")
        LocalDate startDate,
        @Schema(description="예약 조회 종료 날짜", example = "2026-03-31")
        LocalDate endDate,
        @Schema(description="예약 상태", example = "CONFIRMED")
        List<ReservationStatus> status,
        @Schema(description = "페이지 번호 (0부터 시작)", example="0")
        @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
        int page,
        @Schema(description = "페이지 크기", example="20")
        @Positive(message = "페이지 크기는 1 이상이어야 합니다.")
        int size
){
}
