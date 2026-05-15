package com.example.demo.global.auth;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
