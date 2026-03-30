package com.example.demo.domain.favorite.dto;

import com.example.demo.domain.favorite.domain.Favorite;
import com.example.demo.domain.favorite.domain.FavoriteStatus;

public record FavoriteResponse(
        Long id,
        Long userId,
        String StoreName,
        FavoriteStatus status
) {
    public static FavoriteResponse from(Favorite favorite){
        return new FavoriteResponse(
                favorite.getId(),
                favorite.getUser().getId(),
                favorite.getStore().getName(),
                favorite.getStatus()
        );
    }
}
