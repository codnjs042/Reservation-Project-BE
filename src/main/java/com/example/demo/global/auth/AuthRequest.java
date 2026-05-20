package com.example.demo.global.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @Schema(example="test1234")
        @NotBlank(message="아이디를 입력해 주세요.")
        String username,
        @Schema(example="pw1234!!")
        @NotBlank(message="비밀번호를 입력해 주세요.")
        String password
) {
}
