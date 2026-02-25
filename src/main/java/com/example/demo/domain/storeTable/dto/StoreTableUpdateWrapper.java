package com.example.demo.domain.storeTable.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record StoreTableUpdateWrapper(
        @Schema(description = "테이블 목록", example =
                """
                    [
                        {
                            "id": 1,
                            "tableName": "4인석",
                            "minCapacity": 2,
                            "maxCapacity": 4,
                            "count": 2,
                            "status": "ACTIVE"
                        },
                        {
                            "id": 2,
                            "tableName": "2인석",
                            "minCapacity": 1,
                            "maxCapacity": 2,
                            "count": 2,
                            "status": "ACTIVE"
                        }
                    ]
                """)
        List<StoreTableUpdateRequest> updateTables
) {}
