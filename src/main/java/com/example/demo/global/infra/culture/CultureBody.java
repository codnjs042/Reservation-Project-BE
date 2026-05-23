package com.example.demo.global.infra.culture;

record CultureBody(
        CultureItems items,
        String numOfRows,
        String pageNo,
        String totalCount
) {}