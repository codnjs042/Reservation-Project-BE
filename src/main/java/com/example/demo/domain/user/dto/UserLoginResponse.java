package com.example.demo.domain.user.dto;

public record UserLoginResponse(
        String accessToken,
        String tokenType
) {
}
