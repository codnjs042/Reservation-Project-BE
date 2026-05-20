package com.example.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserSignupRequest(
        @Schema(example="test1234")
        @NotBlank(message = "아이디를 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message = "아이디는 8자 이상 15자 이하의 숫자/영문으로 입력해 주세요.")
        String username,
        @Schema(example = "testUser")
        @NotBlank(message = "닉네임을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,15}$", message = "닉네임은 2자 이상 15자 이하의 숫자/영문/한글로 입력해 주세요.")
        String nickname,
        @Schema(example = "test1234@test.com")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @Schema(example = "pw1234!!")
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9!@#%^&*]{8,15}$", message = "비밀번호는 8자 이상 15자 이하의 영문, 숫자, 특수문자(!@#%^&*) 조합으로 입력해 주세요.")
        String password
) {}
