package com.example.demo.domain.reservation.controller;

import com.example.demo.domain.reservation.dto.ReservationCreateRequest;
import com.example.demo.domain.reservation.dto.ReservationSearchRequest;
import com.example.demo.domain.reservation.dto.ReservationTimeSlotResponse;
import com.example.demo.domain.reservation.dto.ReservationTimeSlotRequest;
import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @GetMapping
    public ResponseEntity<List<ReservationTimeSlotResponse>> getTimeSlot(@ModelAttribute ReservationTimeSlotRequest dto,
                                                                         @PathVariable Long storeId){
        List<ReservationTimeSlotResponse> response = reservationService.getTimeSlot(storeId, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="예약 신청", description="입력 받은 예약 정보를 DB에 저장")
    @PostMapping
    public ResponseEntity<String> reserveTime(@RequestBody ReservationCreateRequest dto,
                                              @PathVariable Long storeId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails){
        reservationService.reserveTime(userDetails.getUser(), storeId, dto);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary="예약 조회", description="입력 받은 가게의 예약 조회")
    @GetMapping
    public ResponseEntity<String> search(
            @ModelAttribute ReservationSearchRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        response = reservationService.getReservation(userDetails.getId(), storeId);
        return ResponseEntity.ok(response);
    }
}
