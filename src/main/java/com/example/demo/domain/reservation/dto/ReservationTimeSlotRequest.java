package com.example.demo.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ReservationTimeSlotRequest(
        @Schema(description="인원수", example="2")
        @NotNull(message = "인원수를 입력해 주세요.")
        @Positive(message = "인원수는 1명 이상 가능합니다.")
        int headCount,
        @Schema(description="예약 날짜", example="2026-02-01")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "예약할 날짜를 선택해 주세요.")
        @FutureOrPresent(message = "이미 지난 날짜는 예약 대상이 아닙니다.")
        LocalDate targetDate
) {}
