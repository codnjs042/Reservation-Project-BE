package com.example.demo.domain.storeTable.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreTableStatus {
    ACTIVE("활성화"),
    DELETED("비활성화");

    private final String desc;
}
