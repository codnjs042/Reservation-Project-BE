package com.example.demo.domain.reservation.dto;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReservationSearchUserResponse(
        @Schema(description="예약 번호")
        Long id,
        @Schema(description="상호명")
        String storeName,
        @Schema(description="예약자명")
        String name,
        @Schema(description="예약 날짜 시간")
        LocalDateTime targetDateTime,
        @Schema(description="인원 수")
        int headCount,
        @Schema(description="예약 상태")
        ReservationStatus status
) {
    public static ReservationSearchUserResponse from(Reservation reservation){
        return new ReservationSearchUserResponse(
                reservation.getId(),
                reservation.getStore().getName(),
                reservation.getName(),
                reservation.getTargetDateTime(),
                reservation.getHeadCount(),
                reservation.getStatus()
        );
    }
}