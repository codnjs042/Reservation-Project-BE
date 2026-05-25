package com.example.demo.domain.admin.dto;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;

import java.time.LocalDateTime;

public record StoreAdminResponse(
        Long id,
        String name,
        String username,
        String ownerName,
        StoreStatus status,
        LocalDateTime createdAt
) {
    public static StoreAdminResponse from(Store store){
        return new StoreAdminResponse(
                store.getId(),
                store.getName(),
                store.getOwner().getUsername(),
                store.getOwnerName(),
                store.getStatus(),
                store.getCreatedAt()
        );
    }
}
