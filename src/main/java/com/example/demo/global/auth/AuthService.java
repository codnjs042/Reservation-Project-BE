package com.example.demo.global.auth;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.util.JwtUtil;
import com.example.demo.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    public TokenDto login(AuthRequest dto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.username(), dto.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername(), role);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        redisUtil.set(
                "refresh-token:" + userDetails.getUsername(),
                refreshToken,
                jwtUtil.getRefreshTokenExpiration()
        );

        return new TokenDto(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken){
        if(!jwtUtil.isTokenValid(refreshToken)){
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String username = jwtUtil.extractUsername(refreshToken);

        String savedToken = redisUtil.get("refresh-token:" + username);
        if(savedToken == null || !savedToken.equals(refreshToken)){
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findByUsernameAndDeletedVersion(username, 0L)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtUtil.generateAccessToken(username, "ROLE_" + user.getRole().name());

        return new TokenResponse(newAccessToken);
    }

    public void logout(String refreshToken, String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            if (jwtUtil.isTokenValid(accessToken)) {
                long remainingTtl = jwtUtil.extractRemainingTtl(accessToken);
                if (remainingTtl > 0) {
                    redisUtil.set("blacklist:" + accessToken, "logout", remainingTtl);
                }
            }
        }

        if (jwtUtil.isTokenValid(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            redisUtil.delete("refresh-token:" + username);
        }
    }
}
