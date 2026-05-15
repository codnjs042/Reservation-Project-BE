package com.example.demo.global.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @Schema(example="test1234@test.com")
        @NotBlank(message="이메일을 입력해 주세요.")
        String email,
        @Schema(example="pw1234!!")
        @NotBlank(message="비밀번호를 입력해 주세요.")
        String password
) {
}
