package com.example.demo.domain.admin.dto;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationAdminResponse(
        Long id,
        String name,
        String storeName,
        LocalDateTime targetDateTime,
        int headCount,
        Long storeTableId,
        ReservationStatus status
) {
    public static ReservationAdminResponse from(Reservation reservation) {
        return new ReservationAdminResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getStore().getName(),
                reservation.getTargetDateTime(),
                reservation.getHeadCount(),
                reservation.getStoreTable().getId(),
                reservation.getStatus()
        );
    }
}
