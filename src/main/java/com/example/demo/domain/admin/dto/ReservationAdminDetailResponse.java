package com.example.demo.domain.admin.dto;

import com.example.demo.domain.reservation.domain.Reservation;
import com.example.demo.domain.reservation.domain.ReservationStatus;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public record ReservationAdminDetailResponse(
        Long id,
        String userName,
        String name,
        String storeName,
        LocalDateTime targetDateTime,
        DayOfWeek dayOfWeek,
        int headCount,
        Long storeTableId,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReservationAdminDetailResponse from(Reservation reservation) {
        return new ReservationAdminDetailResponse(
                reservation.getId(),
                reservation.getUser().getUsername(),
                reservation.getName(),
                reservation.getStore().getName(),
                reservation.getTargetDateTime(),
                reservation.getDayOfWeek(),
                reservation.getHeadCount(),
                reservation.getStoreTable().getId(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}