package com.example.demo.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ScheduleUpsertRequest(
        @Schema(description="시작 시간", example="09:00:00")
        LocalTime startTime,
        @Schema(description="종료 시간", example="21:00:00")
        LocalTime endTime,
        @Schema(description="예약 간격(분)", example="60")
        int intervalMinute
) {
}
