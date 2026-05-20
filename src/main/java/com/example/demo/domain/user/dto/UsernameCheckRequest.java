package com.example.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UsernameCheckRequest(
        @Schema(description = "아이디 중복 검사", example="test1234")
        @NotBlank(message = "아이디를 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message = "아이디는 8자 이상 15자 이하의 숫자/영문으로 입력해 주세요.")
        String username
) {
}
