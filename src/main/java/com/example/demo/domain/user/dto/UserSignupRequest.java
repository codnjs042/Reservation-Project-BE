package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserSignupRequest(
        @Schema(example="test1234@test.com")
        String email,
        @Schema(example="testUser")
        String nickname,
        @Schema(example="pw1234!")
        String password,
        @Schema(description="사용자 권한", example="USER", allowableValues = {"USER", "OWNER"})
        UserRole role
) {}
