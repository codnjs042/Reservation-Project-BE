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

import java.util.List;

@Tag(name="Admin API", description ="관리자 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AdminFacade adminFacade;

    @Operation(summary="유저 관리", description="유저 목록 반환")
    @GetMapping("/users")
    public ResponseEntity<List<UserAdminResponse>> getUsers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute UserAdminRequest dto
    ){
        List<UserAdminResponse> response = userService.getUsersForAdmin(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 관리", description="가게 목록 반환")
    @GetMapping("/stores")
    public ResponseEntity<List<StoreAdminResponse>> getStores(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute StoreAdminRequest dto
    ){
        List<StoreAdminResponse> response = adminFacade.getStoresForAdmin(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 관리", description="예약 목록 반환")
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationAdminResponse>> getStores(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute ReservationAdminRequest dto
    ){
        List<ReservationAdminResponse> response = adminFacade.getReservationsForAdmin(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }
}
