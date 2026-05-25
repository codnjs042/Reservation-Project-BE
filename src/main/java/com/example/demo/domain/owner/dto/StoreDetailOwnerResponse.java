package com.example.demo.domain.owner.dto;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;

public record StoreDetailOwnerResponse(
        Long id,
        String name,
        StoreCategory category,
        String address,
        String detailAddress,
        String zipCode,
        String sigunguCode,
        String phone,
        String businessNumber,
        int favorites,
        StoreStatus status,
        boolean hasSchedule,
        boolean hasTable
) {
    public boolean isReadyToOpen(){
        return hasSchedule && hasTable;
    }
    public static StoreDetailOwnerResponse from(Store store, boolean hasSchedule, boolean hasTable){
        return new StoreDetailOwnerResponse(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getAddress(),
                store.getDetailAddress(),
                store.getZipCode(),
                store.getSigunguCode(),
                store.getPhone(),
                store.getBusinessNumber(),
                store.getFavorites(),
                store.getStatus(),
                hasSchedule,
                hasTable
        );
    }
}
