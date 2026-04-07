package com.example.demo.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("유저"),
    OWNER("점주"),
    ADMIN("관리자");

    private final String desc;
}
