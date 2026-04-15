package com.example.demo.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.util.List;

public record ScheduleUpsertWrapper(
        @Schema(description = "영업 시간대 목록", example =
                """
                    [
                        {
                            "startTime": "09:00:00",
                            "endTime": "15:00:00",
                            "intervalMinute": "60"
                        },
                        {
                            "startTime": "18:00:00",
                            "endTime": "21:00:00",
                            "intervalMinute": "30"
                        }
                    ]
                """
        )
        @Valid
        List<ScheduleUpsertRequest> upsertSchedules
) {
}
