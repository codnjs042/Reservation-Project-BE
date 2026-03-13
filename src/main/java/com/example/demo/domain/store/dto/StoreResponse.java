package com.example.demo.domain.store.dto;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;

public record StoreResponse(
        Long id,
        String name,
        StoreCategory category,
        String address,
        String phone,
        int slotInterval,
        int favorites,
        StoreStatus status
) {
    public static StoreResponse from(Store store){
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getAddress(),
                store.getPhone(),
                store.getSlotInterval(),
                store.getFavorites(),
                store.getStatus()
        );
    }
}
