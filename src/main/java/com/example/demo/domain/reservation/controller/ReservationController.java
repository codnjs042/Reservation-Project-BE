package com.example.demo.domain.reservation.controller;

import com.example.demo.domain.reservation.dto.*;
import com.example.demo.domain.reservation.service.ReservationFacade;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Reservation API", description ="가게 예약 관리 API")
@RestController
@RequestMapping("/api/stores/{storeId}/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationFacade reservationFacade;

    @Operation(summary="예약 타임 슬롯 조회", description="입력 받은 날짜와 인원 수를 토대로 시간대별 예약 가능 현황 반환.")
    @GetMapping("/time-slot")
    public ResponseEntity<List<ReservationTimeSlotResponse>> getTimeSlot(
            @Valid @ModelAttribute ReservationTimeSlotRequest dto,
            @PathVariable Long storeId)
    {
        List<ReservationTimeSlotResponse> response = reservationFacade.getTimeSlots(storeId, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 신청", description="입력 받은 예약 정보를 DB에 저장")
    @PostMapping
    public ResponseEntity<ReservationCreateResponse> reserveTime(
            @Valid @RequestBody ReservationCreateRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        ReservationCreateResponse response = reservationFacade.reserve(userDetails.getUser(), storeId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
