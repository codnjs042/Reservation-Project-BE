package com.example.demo.domain.store.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreCategory {
    KOREAN("한식"),
    SNACK("분식"),
    CHICKEN("치킨"),
    ASIAN("동양식"),
    WESTERN("서양식"),
    FASTFOOD("패스트푸드"),
    BUFFET("뷔페"),
    FUSION("퓨전");

    private final String desc;
}
