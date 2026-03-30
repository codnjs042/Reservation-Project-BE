package com.example.demo.domain.store.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreStatus {
    READY("준비중"),
    OPEN("영업중"),
    HIDDEN("일시중지"),
    SHUTDOWN("폐업");

    private final String desc;
}