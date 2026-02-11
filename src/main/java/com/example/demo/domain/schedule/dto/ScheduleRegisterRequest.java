package com.example.demo.domain.schedule.dto;

import com.example.demo.domain.schedule.domain.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ScheduleRegisterRequest(
        @Schema(description="요일", example="MONDAY")
        DayOfWeek dayOfWeek,
        @Schema(description="시작 시간", example="09:00:00")
        LocalTime startTime,
        @Schema(description="종료 시간", example="21:00:00")
        LocalTime endTime,
        @Schema(description="운영 상태", example="OPEN")
        ScheduleType type
) {
}
