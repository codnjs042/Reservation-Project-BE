package com.example.demo.domain.reservation.controller;

import com.example.demo.domain.reservation.dto.ReservationCreateRequest;
import com.example.demo.domain.reservation.dto.ReservationTimeSlotResponse;
import com.example.demo.domain.reservation.dto.ReservationTimeSlotRequest;
import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.global.security.CustomUserDetails;
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

    @GetMapping
    public ResponseEntity<List<ReservationTimeSlotResponse>> getTimeSlot(@ModelAttribute ReservationTimeSlotRequest dto,
                                                                         @PathVariable Long storeId){
        List<ReservationTimeSlotResponse> response = reservationService.getTimeSlot(storeId, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> reserveTime(@RequestBody ReservationCreateRequest dto,
                                              @PathVariable Long storeId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails){
        reservationService.reserveTime(userDetails.getUser(), storeId, dto);
        return ResponseEntity.ok("완료");
    }
}
