package com.example.demo.domain.favorite.dto;

import com.example.demo.domain.favorite.domain.Favorite;
import com.example.demo.domain.favorite.domain.FavoriteStatus;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.user.domain.User;

public record FavoriteResponse(
        Long id,
        User user,
        Store store,
        FavoriteStatus status
) {
    public static FavoriteResponse from(Favorite favorite){
        return new FavoriteResponse(
                favorite.getId(),
                favorite.getUser(),
                favorite.getStore(),
                favorite.getStatus()
        );
    }
}
