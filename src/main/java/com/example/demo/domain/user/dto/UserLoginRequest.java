package com.example.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginRequest(
        @Schema(example="test1234@test.com")
        String email,
        @Schema(example="pw1234!")
        String password
) {}
