package com.example.demo.global.infra.culture;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public record CultureDto(
        String name,
        String category,
        String address,
        String sigunguCode,
        String latitude,
        String longitude
) {
    static CultureDto from(CultureItem item) {
        String name = Stream.of(item.rstrNm(), item.rstrBhfNm(), item.rstrAsstnNm())
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(" "));

        return new CultureDto(
                name,
                item.rstrClNm(),
                item.rstrRoadAddr(),
                item.rstrPnu() != null ? item.rstrPnu().substring(0, 5) : null,
                item.rstrLatPos(),
                item.rstrLotPos()
        );
    }
}