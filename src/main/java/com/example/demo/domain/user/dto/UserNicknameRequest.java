package com.example.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserNicknameRequest(
        @Schema(description="닉네임", example="돼지")
        @NotBlank(message = "닉네임을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,15}$", message = "닉네임은 2자 이상 15자 이하의 숫자/영문/한글로 입력해 주세요.")
        String nickname
) {
}
