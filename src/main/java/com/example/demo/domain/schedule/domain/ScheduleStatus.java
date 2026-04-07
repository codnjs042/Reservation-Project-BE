package com.example.demo.domain.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleStatus {
    ACTIVE("활성화"),
    DELETED("비활성화");

    private final String desc;
}
