package com.example.demo.domain.store.dto;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;

public record StoreDetailResponse(
        Long id,
        String name,
        StoreCategory category,
        String address,
        String detailAddress,
        String zipCode,
        Double latitude,
        Double longitude,
        String phone,
        int favorites,
        StoreStatus status,
        boolean isFavorite
) {
    public static StoreDetailResponse from(Store store, boolean isFavorite){
        return new StoreDetailResponse(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getAddress(),
                store.getDetailAddress(),
                store.getZipcode(),
                store.getLatitude(),
                store.getLongitude(),
                store.getPhone(),
                store.getFavorites(),
                store.getStatus(),
                isFavorite
        );
    }
}
