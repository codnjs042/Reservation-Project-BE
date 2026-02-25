package com.example.demo.domain.reservation.dto;

import com.example.demo.domain.reservation.domain.Reservation;

import java.time.LocalDateTime;

public record ReservationCreateResponse(
        Long id,
        String name,
        int headCount,
        LocalDateTime targetDateTime
) {
    public static ReservationCreateResponse from(Reservation reservation){
        return new ReservationCreateResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getHeadCount(),
                reservation.getTargetDateTime()
        );
    }
}
