package com.example.demo.domain.user.controller;

import com.example.demo.domain.favorite.dto.FavoriteResponse;
import com.example.demo.domain.favorite.serivce.FavoriteService;
import com.example.demo.domain.reservation.dto.ReservationSearchRequest;
import com.example.demo.domain.reservation.dto.ReservationSearchResponse;
import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.domain.user.dto.*;
import com.example.demo.domain.user.service.UserFacade;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="User API", description ="유저 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserFacade userFacade;
    private final FavoriteService favoriteService;
    private final ReservationService reservationService;

    @Operation(summary="이메일 중복 검사", description="입력 받은 이메일이 DB에 존재하는지 확인. 중복 시 true 반환")
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(
            @Valid @ModelAttribute EmailCheckRequest dto)
    {
        boolean response = userService.check(dto.email());
        return ResponseEntity.ok(response);
    }

    @Operation(summary="회원 가입", description="입력 받은 유저 정보를 DB에 저장. 성공 시 유저 정보 반환")
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponse> signup(
            @Valid @RequestBody UserSignupRequest dto)
    {
        UserSignupResponse response = userService.join(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="닉네임 변경", description="현재 로그인 상태의 유저에게 입력 받은 닉네임을 db에 적용")
    @PatchMapping("/me/nickname")
    public ResponseEntity<String> updateNickname(
            @Valid @RequestBody UserNicknameRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        userService.updateNickname(userDetails.getId(), dto.nickname());
        return ResponseEntity.ok("닉네임 변경 성공 : " + dto.nickname());
    }

    @Operation(summary="비밀번호 변경", description="현재 로그인 상태의 유저에게 입력 받은 비밀번호를 db에 적용")
    @PatchMapping("/me/password")
    public ResponseEntity<String> updatePassword(
            @Valid @RequestBody UserPasswordRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        userService.updatePassword(userDetails.getId(), dto);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth!=null)
            new SecurityContextLogoutHandler().logout(request, response, auth);
        return ResponseEntity.ok("비밀번호 변경 성공");
    }

    @Operation(summary="내 정보 조회", description="현재 로그인한 유저의 정보 반환")
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        UserProfileResponse response = UserProfileResponse.from(userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @Operation(summary="관심 가게 목록 조회", description="현재 로그인 상태의 유저의 관심 가게 목록 조회")
    @GetMapping("/me/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        List<FavoriteResponse> response = favoriteService.getList(userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 조회(유저용)", description="현재 로그인된 유저의 예약 목록 조회")
    @GetMapping("/me/reservations")
    public ResponseEntity<List<ReservationSearchResponse>> searchUser(
            @Valid @ModelAttribute ReservationSearchRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        List<ReservationSearchResponse> response = reservationService.getMyReservation(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 취소(유저용)", description="DB에 저장된 정보의 상태 변경")
    @PatchMapping("/me/reservations/{reservationId}")
    public ResponseEntity<String> cancel(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        reservationService.cancelReservation(userDetails.getId(), reservationId);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="계정 삭제", description="현재 로그인 상태의 유저의 계정을 비활성화 상태로 변경")
    @DeleteMapping("/me")
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
    {
        userFacade.delete(userDetails.getId());
        if(authentication!=null)
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ResponseEntity.ok("성공");
    }
}
