package com.example.demo.domain.schedule.controller;

import com.example.demo.domain.schedule.dto.ScheduleRegisterRequest;
import com.example.demo.domain.schedule.service.ScheduleService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name="Schedule API", description ="영업 시간 관리 API")
@RestController
@RequestMapping("/store/{storeId}/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody ScheduleRegisterRequest dto,
                                                         @PathVariable Long storeId,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails){
        scheduleService.register(userDetails.getId(), storeId, dto);

        return ResponseEntity.ok("완료");
    }

}
