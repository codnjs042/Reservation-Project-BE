package com.example.demo.domain.owner.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;

import java.util.List;

public record ReservationUpdateOwnerRequest(
        List<Long> ids,
        ReservationStatus status
) {
}
