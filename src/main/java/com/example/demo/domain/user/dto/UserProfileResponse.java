package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;

public record UserProfileResponse(
     String email,
     String nickname,
     UserLoginType loginType,
     String providerId,
     UserRole role
) {
    public static UserProfileResponse from(User user){
        return new UserProfileResponse(
                user.getEmail(),
                user.getNickname(),
                user.getLoginType(),
                user.getProviderId(),
                user.getRole()
        );
    }
}
