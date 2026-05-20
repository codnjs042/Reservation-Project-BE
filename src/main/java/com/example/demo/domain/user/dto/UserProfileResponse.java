package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;

public record UserProfileResponse(
        String username,
        String nickname,
        String email,
        UserLoginType loginType,
        String providerId,
        UserRole role
) {
    public static UserProfileResponse from(User user){
        return new UserProfileResponse(
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getLoginType(),
                user.getProviderId(),
                user.getRole()
        );
    }
}
