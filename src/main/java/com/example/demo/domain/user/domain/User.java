package com.example.demo.domain.user.domain;

import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private UserLoginType loginType;

    @Column
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Builder
    public User(String email, String nickname, String password, UserLoginType loginType, String providerId, UserRole role, UserStatus status){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.loginType = loginType;
        this.providerId = providerId;
        this.role = role;
        this.status = status;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updatePassword(String password){
        this.password = password;
    }
}

