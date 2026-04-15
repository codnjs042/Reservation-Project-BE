package com.example.demo.domain.owner.dto;

import com.example.demo.domain.reservation.domain.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReservationUpdateOwnerRequest(
        @Schema(description="예약 상태 변경 대상 id 목록", example = "[1,2]")
        @NotEmpty(message = "변경 대상을 1개 이상 선택해 주세요.")
        List<Long> ids,
        @Schema(description="예약 상태", example="REJECT", allowableValues = {"REJECT", "VISITED", "NO_SHOW"})
        @NotNull(message = "예약 상태를 선택해 주세요.")
        ReservationStatus status
) {
}
