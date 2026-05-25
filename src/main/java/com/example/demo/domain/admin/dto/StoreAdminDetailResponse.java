package com.example.demo.domain.admin.dto;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;

import java.time.LocalDateTime;

public record StoreAdminDetailResponse(
        Long id,
        String name,
        StoreCategory category,
        String address,
        String detailAddress,
        String zipcode,
        String phone,
        String ownerName,
        String ownerUsername,
        String businessNumber,
        int favorites,
        StoreStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static StoreAdminDetailResponse from(Store store) {
        return new StoreAdminDetailResponse(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getAddress(),
                store.getDetailAddress(),
                store.getZipCode(),
                store.getPhone(),
                store.getOwnerName(),
                store.getOwner().getUsername(),
                store.getBusinessNumber(),
                store.getFavorites(),
                store.getStatus(),
                store.getCreatedAt(),
                store.getUpdatedAt()
        );
    }
}