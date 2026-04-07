package com.example.demo.domain.admin.dto;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.domain.store.dto.StoreDetailResponse;

public record StoreAdminResponse(
        Long id,
        String name,
        StoreCategory category,
        String address,
        String phone,
        String ownerName,
        String businessNumber,
        int favorites,
        StoreStatus status
) {
    public static StoreAdminResponse from(Store store){
        return new StoreAdminResponse(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getAddress(),
                store.getPhone(),
                store.getOwnerName(),
                store.getBusinessNumber(),
                store.getFavorites(),
                store.getStatus()
        );
    }
}
