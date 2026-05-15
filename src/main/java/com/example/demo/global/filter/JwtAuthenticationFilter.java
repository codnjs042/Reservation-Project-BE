package com.example.demo.global.filter;

import com.example.demo.global.provider.JwtTokenProvider;
import com.example.demo.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");
        String token = null;

        // 1. 헤더에서 토큰 추출 확인 로그
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            // System.out.println("Extracted Token: " + token); // 토큰이 잘 오는지 확인
        }

        if (StringUtils.hasText(token)) {
            try {
                if (jwtUtil.isTokenValid(token)) {
                    // 2. 토큰이 유효할 경우 인증 객체 생성
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 인증 성공 로그 (이게 찍혀야 403이 뜹니다)
                    System.out.println("인증 성공: " + authentication.getName() + ", 권한: " + authentication.getAuthorities());
                } else {
                    // 3. 토큰은 있는데 유효하지 않을 경우 (만료 등)
                    System.out.println("인증 실패: 유효하지 않은 토큰입니다.");
                }
            } catch (Exception e) {
                // 4. 검증 과정에서 예외 발생 시 로그
                System.out.println("인증 처리 중 에러 발생: " + e.getMessage());
            }
        } else {
            // 5. 토큰이 아예 없는 경우
            System.out.println("인증 정보 없음: Authorization 헤더가 비어있거나 올바르지 않습니다.");
        }

        filterChain.doFilter(request, response);
    }
}
