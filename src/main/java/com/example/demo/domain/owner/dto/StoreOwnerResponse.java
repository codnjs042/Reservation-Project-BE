package com.example.demo.domain.owner.dto;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;

public record StoreOwnerResponse(
        Long id,
        String name,
        int favorites,
        StoreStatus status,
        boolean hasSchedule,
        boolean hasTable
) {
    public boolean isReadyToOpen(){
        return hasSchedule && hasTable;
    }
    public static StoreOwnerResponse from(Store store, boolean hasSchedule, boolean hasTable){
        return new StoreOwnerResponse(
                store.getId(),
                store.getName(),
                store.getFavorites(),
                store.getStatus(),
                hasSchedule,
                hasTable
        );
    }
}
