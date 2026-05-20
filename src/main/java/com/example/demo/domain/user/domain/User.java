package com.example.demo.domain.user.domain;

import com.example.demo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_username_deleted_version",
                columnNames = {"username", "deleted_version"}
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Column
    private String email;

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

    @Column(nullable = false)
    private Long deletedVersion;

    @Builder
    public User(String username, String nickname, String email, String password, UserLoginType loginType, String providerId, UserRole role, UserStatus status){
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.loginType = loginType;
        this.providerId = providerId;
        this.role = role;
        this.status = status;
        this.deletedVersion = 0L;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateEmail(String email){
        this.email = email;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateRole(UserRole role){
        this.role = role;
    }

    public void updateStatus(UserStatus status){
        this.status = status;
    }

    public void updateVersion(){
        this.deletedVersion = this.id;
    }
}

