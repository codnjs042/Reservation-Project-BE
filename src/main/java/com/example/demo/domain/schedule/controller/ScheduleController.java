package com.example.demo.domain.schedule.controller;

import com.example.demo.domain.schedule.dto.ScheduleRegisterRequest;
import com.example.demo.domain.schedule.service.ScheduleFacade;
import com.example.demo.domain.schedule.service.ScheduleService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name="Schedule API", description ="영업 시간 관리 API")
@RestController
@RequestMapping("/store/{storeId}/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleFacade scheduleFacade;

    @Operation(summary="영업 시간 등록", description="입력 받은 시간 정보를 DB에 저장")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody ScheduleRegisterRequest dto,
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        scheduleFacade.register(userDetails.getId(), storeId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
