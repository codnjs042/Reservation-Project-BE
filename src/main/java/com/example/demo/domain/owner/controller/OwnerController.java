package com.example.demo.domain.owner.controller;

import com.example.demo.domain.owner.dto.*;
import com.example.demo.domain.owner.service.OwnerFacade;
import com.example.demo.domain.owner.dto.ReservationSearchOwnerResponse;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@Tag(name="Owner API", description ="가게 운영 API")
@RestController
@RequestMapping("/api/owners/stores")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerFacade ownerFacade;

    @Operation(summary="가게 목록", description="현재 로그인한 유저 권한의 가게 목록 반환.")
    @GetMapping
    public ResponseEntity<Page<StoreOwnerResponse>> getStores(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute StoreOwnerRequest dto)
    {
        Page<StoreOwnerResponse> response = ownerFacade.getStores(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 상세 정보", description="현재 로그인한 유저 권한의 가게 상세 정보 반환.")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailOwnerResponse> getStoreDetail(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        StoreDetailOwnerResponse response = ownerFacade.getStoreDetail(userDetails.getId(), storeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="가게 삭제", description="현재 로그인한 유저 권한의 가게를 삭제 처리.")
    @DeleteMapping
    public ResponseEntity<Void> deleteStores(
            @Valid @RequestBody StoreDeleteRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        ownerFacade.delete(userDetails.getId(), dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="가게 수정", description="현재 로그인한 유저 권한의 가게의 정보를 변경")
    @PatchMapping("/{storeId}")
    public ResponseEntity<String> updateStoreInfo(
            @Valid @RequestBody StoreInfoUpdateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        ownerFacade.updateStoreInfo(userDetails.getId(), storeId, dto);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="가게 영업 상태 변경", description="현재 로그인한 유저 권한의 가게의 정보를 변경")
    @PatchMapping("/{storeId}/status")
    public ResponseEntity<String> updateStoreStatus(
            @Valid @RequestBody StoreStatusUpdateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        ownerFacade.updateStoreStatus(userDetails.getId(), storeId, dto);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="예약 조회", description="현재 로그인된 유저 권한의 가게 예약 목록 조회")
    @GetMapping("/{storeId}/reservations")
    public ResponseEntity<Page<ReservationSearchOwnerResponse>> getReservations(
            @Valid @ModelAttribute ReservationSearchOwnerRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Page<ReservationSearchOwnerResponse> response = ownerFacade.getStoreReservation(userDetails.getId(), storeId, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 관리", description="현재 로그인된 유저 권한의 가게 예약 상태 일괄 변경")
    @PatchMapping("/{storeId}/reservations")
    public ResponseEntity<String> updateReservationStatus(
            @Valid @RequestBody ReservationUpdateOwnerRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        ownerFacade.updateReservationStatus(userDetails.getId(), storeId, dto);
        return ResponseEntity.ok("완료");
    }
}
