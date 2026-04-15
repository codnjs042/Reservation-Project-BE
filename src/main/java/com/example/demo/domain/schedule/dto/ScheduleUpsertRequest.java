package com.example.demo.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalTime;

public record ScheduleUpsertRequest(
        @Schema(description="시작 시간", example="09:00:00")
        @NotNull(message = "시작 시간을 입력해 주세요.")
        LocalTime startTime,
        @Schema(description="종료 시간", example="21:00:00")
        @NotNull(message = "종료 시간을 입력해 주세요.")
        LocalTime endTime,
        @Schema(description="예약 간격(분)", example="60")
        @NotNull(message = "예약 간격(분)을 입력해 주세요.")
        @Positive(message = "예약 간격은 최소 1분입니다.")
        int intervalMinute
) {
}
