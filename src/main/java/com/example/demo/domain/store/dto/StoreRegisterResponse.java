package com.example.demo.domain.store.dto;

import com.example.demo.domain.store.domain.Store;

public record StoreRegisterResponse(
        Long id,
        String name) {
    public static StoreRegisterResponse from(Store store){
        return new StoreRegisterResponse(
                store.getId(),
                store.getName()
        );
    }
}
