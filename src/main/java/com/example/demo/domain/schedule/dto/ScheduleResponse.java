package com.example.demo.domain.schedule.dto;

import com.example.demo.domain.schedule.domain.Schedule;
import com.example.demo.domain.schedule.domain.ScheduleStatus;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ScheduleResponse(
        Long id,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        int intervalMinute,
        ScheduleStatus status
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getIntervalMinute(),
                schedule.getStatus()
        );
    }
}
