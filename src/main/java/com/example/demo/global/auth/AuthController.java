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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(summary="로그인", description="입력 받은 아이디, 비밀 번호가 DB에 존재하는지 확인")
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
            @CookieValue(name = "refresh_token") String refreshToken)
    {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary="로그아웃", description="로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refresh_token") String refreshToken,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response)
    {
        authService.logout(refreshToken, authHeader);
        jwtUtil.removeTokenCookie(response);
        return ResponseEntity.ok().build();
    }
}
