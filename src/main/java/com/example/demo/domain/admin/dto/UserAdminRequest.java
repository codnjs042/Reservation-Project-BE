package com.example.demo.domain.admin.dto;

import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.domain.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UserAdminRequest(
        @Schema(description = "검색어(회원번호/이메일/닉네임)", example="1")
        @Size(max=50, message= "검색어는 50자 이내로 입력해 주세요.")
        String keyword,
        @Schema(description = "로그인타입", example="LOCAL")
        UserLoginType loginType,
        @Schema(description = "권한", example="USER")
        UserRole role,
        @Schema(description = "상태", example="ACTIVE")
        UserStatus status
) {
}
