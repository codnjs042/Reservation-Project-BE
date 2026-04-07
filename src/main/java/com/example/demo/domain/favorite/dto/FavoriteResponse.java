package com.example.demo.domain.favorite.dto;

import com.example.demo.domain.favorite.domain.Favorite;
import com.example.demo.domain.store.dto.StoreResponse;

public record FavoriteResponse(
        Long id,
        StoreResponse storeResponse
) {
    public static FavoriteResponse from(Favorite favorite){
        return new FavoriteResponse(
                favorite.getId(),
                StoreResponse.from(favorite.getStore())
        );
    }
}
