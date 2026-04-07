package com.example.demo.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("활성화"),
    DELETED("비활성화");

    private final String desc;
}
