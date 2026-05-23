package com.example.demo.global.filter;

import com.example.demo.global.provider.JwtTokenProvider;
import com.example.demo.global.util.JwtUtil;
import com.example.demo.global.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (StringUtils.hasText(token)) {
            try {
                if (jwtUtil.isTokenValid(token) && !redisUtil.exists("blacklist:" + token)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("인증 성공: {}, 권한: {}", authentication.getName(), authentication.getAuthorities());
                } else {
                    log.warn("인증 실패: 유효하지 않은 토큰입니다.");
                }
            } catch (Exception e) {
                log.error("인증 처리 중 에러 발생: ", e);
            }
        } else {
            log.debug("인증 정보 없음: Authorization 헤더가 비어있거나 올바르지 않습니다.");
        }
        filterChain.doFilter(request, response);
    }
}
