package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.UserLoginRequest;
import com.example.demo.domain.user.dto.UserPasswordRequest;
import com.example.demo.domain.user.dto.UserSignupRequest;
import com.example.demo.domain.user.dto.UserSignupResponse;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name="User API", description ="회원가입 및 로그인 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary="이메일 중복 검사", description="입력 받은 이메일이 DB에 존재하는지 확인. 중복 시 true 반환.")
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam @Schema(example="test1234@test.com") String email){
        boolean response = userService.check(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="회원 가입", description="입력 받은 유저 정보를 DB에 저장. 성공 시 유저 정보 반환.")
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponse> signup(@RequestBody UserSignupRequest dto) {
        UserSignupResponse response = userService.join(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="로그인", description="입력 받은 이메일, 비밀 번호가 DB에 존재하는지 확인")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest dto, HttpServletRequest request){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        return ResponseEntity.ok("로그인 성공");
    }

    @Operation(summary="로그아웃", description="현재 세션을 무효화하여 로그아웃 처리")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null)
            session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("로그아웃 성공");
    }

    @Operation(summary="닉네임 변경", description="현재 로그인 상태의 유저에게 입력 받은 닉네임을 db에 적용")
    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@RequestParam @Schema(example="또치") String nickname,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        userService.updateNickname(userDetails.getId(), nickname);
        return ResponseEntity.ok("닉네임 변경 성공 : "+ nickname);
    }

    @Operation(summary="비밀번호 변경", description="현재 로그인 상태의 유저에게 입력 받은 비밀번호를 db에 적용")
    @PatchMapping("/password")
    public ResponseEntity<String> updateNickname(@RequestBody UserPasswordRequest dto,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        userService.updatePassword(userDetails.getId(), dto);
        return ResponseEntity.ok("비밀번호 변경 성공");
    }
}
