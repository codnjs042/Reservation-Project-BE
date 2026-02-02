package com.example.demo.domain.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MenuRegisterRequest(
        @Schema(description="메뉴", example="돼지국밥")
        String name,
        @Schema(description="가격", example="10000")
        int price,
        @Schema(description="일일 한정 수량", example="100")
        int limitCount
) {}
