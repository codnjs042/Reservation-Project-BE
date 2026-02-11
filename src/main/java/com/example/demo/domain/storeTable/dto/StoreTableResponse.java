package com.example.demo.domain.storeTable.dto;

import com.example.demo.domain.storeTable.domain.StoreTable;

public record StoreTableResponse(
        Long id,
        String tableName,
        int minCapacity,
        int maxCapacity,
        int count
) {
    public static StoreTableResponse from(StoreTable storeTable){
        return new StoreTableResponse(
                storeTable.getId(),
                storeTable.getTableName(),
                storeTable.getMinCapacity(),
                storeTable.getMaxCapacity(),
                storeTable.getCount()
        );
    }
}
