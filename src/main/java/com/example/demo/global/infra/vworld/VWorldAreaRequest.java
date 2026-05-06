package com.example.demo.global.infra.vworld;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record VWorldAreaRequest(
        @Schema(description = "지역코드", example = "11")
        @Pattern(regexp = "^(|\\d{2})$", message = "지역코드는 2글자 숫자입니다.")
        String cd
) {
}
