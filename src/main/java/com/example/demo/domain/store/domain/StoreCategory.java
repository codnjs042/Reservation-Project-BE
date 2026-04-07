package com.example.demo.domain.store.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreCategory {
    KOREAN("한식"),
    JAPANESE("일식"),
    CHINESE("중식"),
    WESTERN("양식"),
    ASIAN("아시안");

    private final String desc;
}
