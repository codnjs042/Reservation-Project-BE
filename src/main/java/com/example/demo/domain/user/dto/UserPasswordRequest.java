package com.example.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserPasswordRequest(
        @Schema(description="현재 비밀번호", example="pw1234!!")
        @NotBlank(message = "현재 비밀번호를 입력해 주세요.")
        String currentPw,
        @Schema(description="새 비밀번호", example="newPw1234!")
        @NotBlank(message = "새 비밀번호를 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9!@#%^&*]{8,15}$", message = "비밀번호는 8자 이상 15자 이하의 영문, 숫자, 특수문자(!@#%^&*) 조합으로 입력해 주세요.")
        String newPw,
        @Schema(description="비밀번호 확인", example="newPw1234!")
        @NotBlank(message = "새 비밀번호를 한 번 더 입력해 주세요.")
        String confirmPw
) {
}
