package com.example.demo.domain.admin.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationAdminRequest(
        @Schema(description = "검색어(예약번호/예약자명/가게명)", example="호식이국밥")
        String keyword,
        @Schema(description = "예약상태", example="CONFIRMED")
        ReservationStatus status
) {
}
