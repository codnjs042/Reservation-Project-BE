package com.example.demo.domain.admin.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record ReservationAdminRequest(
        @Schema(description = "검색어(예약번호/예약자명/가게명)", example="호식이국밥")
        @Size(max=50, message= "검색어는 50자 이내로 입력해 주세요.")
        String keyword,
        @Schema(description = "예약상태", example="CONFIRMED")
        ReservationStatus status
) {
}
