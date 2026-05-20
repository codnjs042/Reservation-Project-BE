package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;
import lombok.Builder;

@Builder
public record UserSignupResponse(
        Long id,
        String username,
        String nickname,
        String email,
        UserRole role
) {
    public static UserSignupResponse from(User user){
        return new UserSignupResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getRole()
        );
    }
}
