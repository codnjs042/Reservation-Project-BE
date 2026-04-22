package com.example.demo.domain.store.dto;

import com.example.demo.domain.store.domain.Store;

public record StorePointResponse(
        Long id,
        String name,
        Double latitude,
        Double longitude
) {
    public static StorePointResponse from(Store store){
        return new StorePointResponse(
                store.getId(),
                store.getName(),
                store.getLatitude(),
                store.getLongitude()
        );
    }
}
