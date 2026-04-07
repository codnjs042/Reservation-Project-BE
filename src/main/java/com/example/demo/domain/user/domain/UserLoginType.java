package com.example.demo.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserLoginType {
    LOCAL("일반"),
    KAKAO("카카오"),
    NAVER("네이버"),
    GOOGLE("구글");

    private final String desc;
}
