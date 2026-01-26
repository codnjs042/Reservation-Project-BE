package com.example.demo.domain.user.domain;

import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column(length=12)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserLoginType loginType;

    @Column
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}

