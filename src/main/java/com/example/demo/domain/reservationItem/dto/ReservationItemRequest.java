package com.example.demo.domain.reservationItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationItemRequest(
        @Schema(description="메뉴 번호", example="1")
        Long menuId,
        @Schema(description="메뉴 개수", example="2")
        int count
) {}
