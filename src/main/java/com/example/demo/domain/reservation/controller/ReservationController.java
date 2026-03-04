package com.example.demo.domain.reservation.controller;

import com.example.demo.domain.reservation.dto.*;
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
@RequestMapping("/store/{storeId}/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary="예약 타임 슬롯 조회", description="입력 받은 날짜와 인원 수를 토대로 시간대별 예약 가능 현황 반환.")
    @GetMapping("/time-slot")
    public ResponseEntity<List<ReservationTimeSlotResponse>> getTimeSlot(
            @ModelAttribute ReservationTimeSlotRequest dto,
            @PathVariable Long storeId){
        List<ReservationTimeSlotResponse> response = reservationService.getTimeSlot(storeId, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 신청", description="입력 받은 예약 정보를 DB에 저장")
    @PostMapping
    public ResponseEntity<ReservationCreateResponse> reserveTime(
            @RequestBody ReservationCreateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        ReservationCreateResponse response = reservationService.reserveTime(userDetails.getUser(), storeId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="예약 조회(가게용)", description="현재 로그인된 유저 소유의 가게 예약 목록 조회")
    @GetMapping
    public ResponseEntity<List<ReservationSearchOwnerResponse>> searchOwner(
            @ModelAttribute ReservationSearchOwnerRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        List<ReservationSearchOwnerResponse> response = reservationService.getStoreReservation(userDetails.getId(), storeId, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 조회(유저용)", description="현재 로그인된 유저의 예약 목록 조회")
    @GetMapping("/me")
    public ResponseEntity<List<ReservationSearchUserResponse>> searchUser(
            @ModelAttribute ReservationSearchUserRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        List<ReservationSearchUserResponse> response = reservationService.getMyReservation(userDetails.getId(), dto);
        return ResponseEntity.ok(response);
    }


    @Operation(summary="예약 취소", description="DB에 저장된 정보의 상태 변경")
    @PatchMapping("/me/{reservationId}")
    public ResponseEntity<String> cancel(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        reservationService.cancelReservation(userDetails.getId(), reservationId);
        return ResponseEntity.ok("완료");
    }
}
