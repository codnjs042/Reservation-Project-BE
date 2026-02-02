package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.domain.UserStatus;
import com.example.demo.domain.user.dto.UserSignupRequest;
import com.example.demo.domain.user.dto.UserSignupResponse;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean check(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserSignupResponse join(UserSignupRequest dto){
        Optional<User> existing = userRepository.findByEmail(dto.email());

        if(existing.isPresent())
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");

        String encode = passwordEncoder.encode(dto.password());

        User user = User.builder()
                .email(dto.email())
                .nickname(dto.nickname())
                .password(encode)
                .loginType(UserLoginType.LOCAL)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return UserSignupResponse.from(user);
    }
}
