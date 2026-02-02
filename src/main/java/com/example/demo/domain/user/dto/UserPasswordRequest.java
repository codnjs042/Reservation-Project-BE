package com.example.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserPasswordRequest(
        @Schema(description="현재 비밀번호", example="pw1234!")
        String currentPw,
        @Schema(description="새 비밀번호", example="newPw1234!")
        String newPw,
        @Schema(description="비밀번호 확인", example="newPw1234!")
        String confirmPw) {
}
