package com.example.demo.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ReservationAvailableResponse(
        @Schema(description="예약 시간")
        LocalTime targetTime,
        @Schema(description="예약 가능 여부")
        boolean isAvailable
) {}
