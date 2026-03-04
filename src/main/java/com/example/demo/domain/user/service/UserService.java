package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserStatus;
import com.example.demo.domain.user.dto.UserPasswordRequest;
import com.example.demo.domain.user.dto.UserSignupRequest;
import com.example.demo.domain.user.dto.UserSignupResponse;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
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
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);

        String encode = passwordEncoder.encode(dto.password());

        User user = User.builder()
                .email(dto.email())
                .nickname(dto.nickname())
                .password(encode)
                .loginType(UserLoginType.LOCAL)
                .role(dto.role())
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return UserSignupResponse.from(user);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.updateNickname(nickname);
    }

    @Transactional
    public void updatePassword(Long userId, UserPasswordRequest dto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(user.getProviderId()!=null)
            throw new BusinessException(ErrorCode.POLICY_VIOLATION);

        if(!passwordEncoder.matches(dto.currentPw(), user.getPassword()))
            throw new BusinessException(ErrorCode.MISMATCH);

        if(dto.newPw().equals(dto.currentPw()))
            throw new BusinessException(ErrorCode.POLICY_VIOLATION);

        if(!dto.confirmPw().equals(dto.newPw()))
            throw new BusinessException(ErrorCode.MISMATCH);

        String encode = passwordEncoder.encode(dto.newPw());

        user.updatePassword(encode);
    }
}
