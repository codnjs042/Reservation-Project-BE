package com.example.demo.domain.schedule.controller;

import com.example.demo.domain.schedule.dto.ScheduleResponse;
import com.example.demo.domain.schedule.dto.ScheduleUpsertWrapper;
import com.example.demo.domain.schedule.service.ScheduleFacade;
import com.example.demo.domain.schedule.service.ScheduleService;
import com.example.demo.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@Tag(name="Schedule API", description ="영업 시간 관리 API")
@RestController
@RequestMapping("/stores/{storeId}/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleFacade scheduleFacade;
    private final ScheduleService scheduleService;

    @Operation(summary="요일별 영업시간대 설정", description="해당 요일의 전체 스케줄을 갱신. 기존 예약이 있는 경우, 변경이 제한됨. 빈 리스트 전송 시 휴무 처리")
    @PutMapping("/{dayOfWeek}")
    public ResponseEntity<String> upsert(
            @RequestBody ScheduleUpsertWrapper dto,
            @PathVariable Long storeId,
            @PathVariable DayOfWeek dayOfWeek,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        scheduleFacade.upsert(userDetails.getId(), storeId, dayOfWeek, dto.upsertSchedules());
        return ResponseEntity.ok().build();
    }

    @Operation(summary="영업시간대 조회", description="해당 가게의 전체 스케줄을 반환")
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getSchedules(
            @PathVariable Long storeId)
    {
        List<ScheduleResponse> response = scheduleService.findSchedules(storeId);
        return ResponseEntity.ok(response);
    }
}
