package com.example.demo.domain.storeTable.dto;

import java.util.List;

public record StoreTableUpdateWrapper(
        List<StoreTableUpdateRequest> updateTables
) {}
