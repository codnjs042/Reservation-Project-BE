package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.UserSignupRequest;
import com.example.demo.domain.user.dto.UserSignupResponse;
import com.example.demo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="User API", description ="회원가입 및 로그인 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary="이메일 중복 검사", description="입력 받은 이메일이 DB에 존재하는지 확인. 중복 시 true 반환.")
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam @Schema(example="test1234@test.com") String email){
        boolean isDuplicate = userService.check(email);
        return ResponseEntity.ok(isDuplicate);
    }

    @Operation(summary="회원 가입", description="입력 받은 유저 정보를 DB에 저장. 성공 시 유저 정보 반환.")
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponse> signup(@RequestBody UserSignupRequest dto) {
        UserSignupResponse response = userService.join(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
