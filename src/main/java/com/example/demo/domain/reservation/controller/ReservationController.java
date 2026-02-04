package com.example.demo.domain.reservation.controller;

import com.example.demo.domain.reservation.dto.ReservationRequest;
import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name="Reservation API", description ="가게 예약 관리 API")
@RestController
@RequestMapping("/store/{storeId}/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<String> reserve(@RequestBody ReservationRequest dto,
                                          @PathVariable Long storeId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        reservationService.reserve(userDetails.getUser(), storeId, dto);

        return ResponseEntity.ok("완료");
    }
}
