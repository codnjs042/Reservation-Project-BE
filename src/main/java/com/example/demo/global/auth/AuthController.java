package com.example.demo.global.auth;

import com.example.demo.global.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Auth API", description ="인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(summary="로그인", description="입력 받은 이메일, 비밀 번호가 DB에 존재하는지 확인")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody AuthRequest dto,
            HttpServletResponse response)
    {
        TokenDto tokenDto = authService.login(dto);
        jwtUtil.addRefreshTokenCookie(response, tokenDto.refreshToken());
        return ResponseEntity.ok(new TokenResponse(tokenDto.accessToken()));
    }

    @Operation(summary="리프레시", description="토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response)
    {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary="로그아웃", description="로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response)
    {
        jwtUtil.removeTokenCookie(response);
        return ResponseEntity.ok().build();
    }
}
