package com.example.demo.domain.reservation.controller;

import com.example.demo.domain.reservation.dto.*;
import com.example.demo.domain.reservation.service.ReservationFacade;
import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Reservation API", description ="가게 예약 관리 API")
@RestController
@RequestMapping("/stores/{storeId}/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationFacade reservationFacade;

    @Operation(summary="예약 타임 슬롯 조회", description="입력 받은 날짜와 인원 수를 토대로 시간대별 예약 가능 현황 반환.")
    @GetMapping("/time-slot")
    public ResponseEntity<List<ReservationTimeSlotResponse>> getTimeSlot(
            @ModelAttribute ReservationTimeSlotRequest dto,
            @PathVariable Long storeId)
    {
        List<ReservationTimeSlotResponse> response = reservationFacade.getTimeSlots(storeId, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 신청", description="입력 받은 예약 정보를 DB에 저장")
    @PostMapping
    public ResponseEntity<ReservationCreateResponse> reserveTime(
            @RequestBody ReservationCreateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        ReservationCreateResponse response = reservationFacade.reserve(userDetails.getUser(), storeId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="예약 조회(가게용)", description="현재 로그인된 유저 소유의 가게 예약 목록 조회")
    @GetMapping
    public ResponseEntity<List<ReservationSearchOwnerResponse>> searchOwner(
            @ModelAttribute ReservationSearchOwnerRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        List<ReservationSearchOwnerResponse> response = reservationFacade.getStoreReservation(userDetails.getId(), storeId, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 거부(가게용)", description="현재 로그인된 유저 소유의 가게 예약 상태 변경")
    @PatchMapping("/{reservationId}/reject")
    public ResponseEntity<String> reject(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        reservationService.rejectReservation(userDetails.getId(), reservationId);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="예약 방문 완료(가게용)", description="현재 로그인된 유저 소유의 가게 예약 상태 변경")
    @PatchMapping("/{reservationId}/visited")
    public ResponseEntity<String> visited(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        reservationService.visitedReservation(userDetails.getId(), reservationId);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="예약 노쇼(가게용)", description="현재 로그인된 유저 소유의 가게 예약 상태 변경")
    @PatchMapping("/{reservationId}/no-show")
    public ResponseEntity<String> noShow(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        reservationService.noShowReservation(userDetails.getId(), reservationId);
        return ResponseEntity.ok("완료");
    }
}
