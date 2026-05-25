package com.example.demo.domain.user.service;

import com.example.demo.domain.admin.dto.UserAdminDetailResponse;
import com.example.demo.domain.admin.dto.UserAdminRequest;
import com.example.demo.domain.admin.dto.UserAdminResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserLoginType;
import com.example.demo.domain.user.domain.UserRole;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void check(String username){
        boolean isExists = userRepository.existsByUsernameAndDeletedVersion(username, 0L);
        if(isExists)
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
    }

    @Transactional
    public UserSignupResponse join(UserSignupRequest dto){
        Optional<User> existing = userRepository.findByUsernameAndDeletedVersion(dto.username(), 0L);

        if(existing.isPresent())
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);

        String encode = passwordEncoder.encode(dto.password());

        User user = User.builder()
                .username(dto.username())
                .nickname(dto.nickname())
                .email(dto.email())
                .password(encode)
                .loginType(UserLoginType.LOCAL)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return UserSignupResponse.from(user);
    }

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateNickname(Long userId, String nickname){
        User user = findById(userId);

        user.updateNickname(nickname);
    }

    @Transactional
    public void updateEmail(Long userId, String email){
        User user = findById(userId);

        user.updateEmail(email);
    }

    @Transactional
    public void updatePassword(Long userId, UserPasswordRequest dto){
        User user = findById(userId);

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

    public void updateRole(Long userId, UserRole userRole){
        User user = findById(userId);

        user.updateRole(userRole);
    }

    public UserAdminDetailResponse getUserDetailForAdmin(Long adminId, Long targetId){
        User admin = findById(adminId);

        if(admin.getRole() != UserRole.ADMIN)
            throw new BusinessException(ErrorCode.FORBIDDEN);

        return UserAdminDetailResponse.from(findById(targetId));
    }

    public Page<UserAdminResponse> getUsersForAdmin(Long userId, UserAdminRequest dto){
        User user = findById(userId);

        if(user.getRole()!=UserRole.ADMIN)
            throw new BusinessException(ErrorCode.FORBIDDEN);

        PageRequest pageable = PageRequest.of(dto.page(), dto.size(), Sort.by("id").descending());
        return userRepository.getUsersForAdmin(dto.keyword(), dto.loginType(), dto.role(), dto.status(), pageable)
                .map(UserAdminResponse::from);
    }

    public User findByIdWithLock(Long userId){
        return userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new  BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
