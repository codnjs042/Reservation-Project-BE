package com.example.demo.domain.storeTable.dto;

import java.util.List;

public record StoreTableRegisterWrapper(
        List<StoreTableRegisterRequest> registerTables
) {}
