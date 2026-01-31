package com.example.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserSignupRequest(
        @Schema(example="test1234@test.com")
        String email,
        @Schema(example="testUser")
        String nickname,
        @Schema(example="password1234!")
        String password
) {}
