package com.example.demo.domain.storeTable.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record StoreTableRegisterWrapper(
        @Schema(description = "테이블 목록", example =
                """
                    [
                        {
                            "tableName": "4인석",
                            "minCapacity": 2,
                            "maxCapacity": 4,
                            "count": 2,
                            "status": "ACTIVE"
                        },
                        {
                            "tableName": "2인석",
                            "minCapacity": 1,
                            "maxCapacity": 2,
                            "count": 2,
                            "status": "ACTIVE"
                        }
                    ]
                """)
        List<StoreTableRegisterRequest> registerTables
) {}
