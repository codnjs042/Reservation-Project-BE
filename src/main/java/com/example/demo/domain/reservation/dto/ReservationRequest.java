package com.example.demo.domain.reservation.dto;

import com.example.demo.domain.reservationItem.dto.ReservationItemRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationRequest(
        @Schema(description="인원 수", example="1")
        int headCount,
        @Schema(description="예약 날짜", example="2026-02-01 09:00:00")
        LocalDateTime targetDateTime,
        @Schema(description="예약 메뉴 리스트")
        List<ReservationItemRequest> items
) {}
