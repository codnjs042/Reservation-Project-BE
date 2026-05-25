package com.example.demo.domain.admin.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ReservationAdminRequest(
        @Schema(description = "검색어(예약번호/예약자명/가게명)", example="호식이국밥")
        @Size(max=50, message= "검색어는 50자 이내로 입력해 주세요.")
        String keyword,
        @Schema(description = "예약상태", example="CONFIRMED")
        ReservationStatus status,
        @Schema(description = "페이지 번호 (0부터 시작)", example="0")
        @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
        int page,
        @Schema(description = "페이지 크기", example="20")
        @Positive(message = "페이지 크기는 1 이상이어야 합니다.")
        int size
) {
}
