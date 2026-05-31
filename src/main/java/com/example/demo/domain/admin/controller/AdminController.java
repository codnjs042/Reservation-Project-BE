package com.example.demo.domain.admin.controller;

import com.example.demo.domain.admin.dto.*;
import com.example.demo.domain.admin.service.AdminFacade;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@Tag(name="Admin API", description ="관리자 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AdminFacade adminFacade;

    @Operation(summary="유저 관리", description="유저 목록 반환")
    @GetMapping("/users")
    public ResponseEntity<Page<UserAdminResponse>> getUsers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute UserAdminRequest dto
    ){
        Page<UserAdminResponse> response = userService.getUsersForAdmin(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 관리", description="가게 목록 반환")
    @GetMapping("/stores")
    public ResponseEntity<Page<StoreAdminResponse>> getStores(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute StoreAdminRequest dto
    ){
        Page<StoreAdminResponse> response = adminFacade.getStoresForAdmin(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 관리", description="예약 목록 반환")
    @GetMapping("/reservations")
    public ResponseEntity<Page<ReservationAdminResponse>> getReservations(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute ReservationAdminRequest dto
    ){
        Page<ReservationAdminResponse> response = adminFacade.getReservationsForAdmin(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="유저 상세 조회", description="특정 유저 상세 정보 반환")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserAdminDetailResponse> getUserDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ){
        UserAdminDetailResponse response = userService.getUserDetailForAdmin(userDetails.getId(), id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 상세 조회", description="특정 가게 상세 정보 반환")
    @GetMapping("/stores/{id}")
    public ResponseEntity<StoreAdminDetailResponse> getStoreDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ){
        StoreAdminDetailResponse response = adminFacade.getStoreForAdmin(userDetails.getId(), id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 상세 조회", description="특정 예약 상세 정보 반환")
    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationAdminDetailResponse> getReservationDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ){
        ReservationAdminDetailResponse response = adminFacade.getReservationForAdmin(userDetails.getId(), id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="유저 영구 정지", description="특정 유저를 영구 정지합니다")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> banUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ){
        adminFacade.banUser(userDetails.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
