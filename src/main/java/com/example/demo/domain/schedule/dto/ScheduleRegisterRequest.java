package com.example.demo.domain.schedule.dto;

import com.example.demo.domain.schedule.domain.ScheduleDayOfWeek;
import com.example.demo.domain.schedule.domain.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ScheduleRegisterRequest(
        @Schema(description="요일", example="MON")
        ScheduleDayOfWeek dayOfWeek,
        @Schema(description="시작 시간", example="9")
        LocalTime startTime,
        @Schema(description="종료 시간", example="9")
        LocalTime endTime,
        @Schema(description="라스트 오더", example="9")
        LocalTime lastOrderTime,
        @Schema(description="운영 상태", example="OPEN")
        ScheduleType type
) {
}
