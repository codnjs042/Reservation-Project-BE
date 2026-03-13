package com.example.demo.domain.schedule.controller;

import com.example.demo.domain.schedule.dto.ScheduleUpsertWrapper;
import com.example.demo.domain.schedule.service.ScheduleFacade;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

@Tag(name="Schedule API", description ="영업 시간 관리 API")
@RestController
@RequestMapping("/store/{storeId}/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleFacade scheduleFacade;

    @Operation(summary="영업 시간대 등록", description="입력 받은 시간 정보를 DB에 저장")
    @PutMapping("/{dayOfWeek}")
    public ResponseEntity<String> upsert(
            @RequestBody ScheduleUpsertWrapper dto,
            @PathVariable Long storeId,
            @PathVariable DayOfWeek dayOfWeek,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        scheduleFacade.upsert(userDetails.getId(), storeId, dayOfWeek, dto.upsertSchedules());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
