package com.example.demo.domain.admin.dto;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.domain.UserStatus;

import java.time.LocalDateTime;

public record UserAdminDetailResponse(
        Long id,
        String username,
        String nickname,
        String email,
        UserLoginType loginType,
        String providerId,
        UserRole role,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserAdminDetailResponse from(User user) {
        return new UserAdminDetailResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getLoginType(),
                user.getProviderId(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}