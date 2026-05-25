package com.example.demo.domain.admin.dto;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationAdminResponse(
        Long id,
        String name,
        String username,
        String storeName,
        LocalDateTime targetDateTime,
        ReservationStatus status
) {
    public static ReservationAdminResponse from(Reservation reservation) {
        return new ReservationAdminResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getUser().getUsername(),
                reservation.getStore().getName(),
                reservation.getTargetDateTime(),
                reservation.getStatus()
        );
    }
}
