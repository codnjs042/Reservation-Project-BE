package com.example.demo.domain.reservation.dto;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReservationSearchOwnerResponse(
        @Schema(description="예약 번호")
        Long id,
        @Schema(description="예약자명")
        String name,
        @Schema(description="예약 날짜 시간")
        LocalDateTime targetDateTime,
        @Schema(description="인원 수")
        int headCount,
        @Schema(description="테이블 번호")
        Long storeTableId,
        @Schema(description="예약 상태")
        ReservationStatus status
) {
    public static ReservationSearchOwnerResponse from(Reservation reservation){
        return new ReservationSearchOwnerResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTargetDateTime(),
                reservation.getHeadCount(),
                reservation.getStoreTable().getId(),
                reservation.getStatus()
        );
    }
}
