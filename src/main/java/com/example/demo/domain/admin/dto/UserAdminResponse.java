package com.example.demo.domain.admin.dto;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.domain.UserStatus;

public record UserAdminResponse(
        Long id,
        String username,
        String nickname,
        String email,
        UserLoginType loginType,
        String providerId,
        UserRole role,
        UserStatus status
) {
    public static UserAdminResponse from(User user){
        return new UserAdminResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getLoginType(),
                user.getProviderId(),
                user.getRole(),
                user.getStatus()
        );
    }
}
