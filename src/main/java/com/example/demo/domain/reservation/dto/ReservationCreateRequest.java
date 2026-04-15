package com.example.demo.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @Schema(description="예약자명", example="김둘리")
        @NotBlank(message = "예약자명을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣a-zA-Z\\s]{1,30}$", message = "예약자명은 1자 이상 30자 이하의 영문/한글/공백으로 입력해 주세요.")
        String name,
        @Schema(description="인원수", example="2")
        @NotNull(message = "인원수를 입력해 주세요.")
        @Positive(message = "인원수는 1명 이상 가능합니다.")
        int headCount,
        @Schema(description="예약 날짜 시간", example="2026-02-01T09:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @NotNull(message = "예약할 날짜와 시간을 선택해 주세요.")
        @Future(message = "이미 지난 시간은 예약할 수 없습니다.")
        LocalDateTime targetDateTime
) {
}
