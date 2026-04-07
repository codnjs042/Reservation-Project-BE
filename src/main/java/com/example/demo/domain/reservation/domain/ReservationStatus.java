package com.example.demo.domain.reservation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    CONFIRMED("수락"),
    CANCELED("취소"),
    REJECTED("거절"),
    NO_SHOW("노쇼"),
    VISITED("방문완료");

    private final String desc;
}