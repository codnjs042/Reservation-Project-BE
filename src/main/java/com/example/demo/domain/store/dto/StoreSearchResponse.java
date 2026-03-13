package com.example.demo.domain.store.dto;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;

public record StoreSearchResponse(
        Long id,
        String name,
        StoreCategory category,
        String address,
        String phone,
        int slotInterval,
        StoreStatus status
) {
    public static StoreSearchResponse from(Store store){
        return new StoreSearchResponse(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getAddress(),
                store.getPhone(),
                store.getSlotInterval(),
                store.getStatus()
        );
    }
}
