package com.example.demo.domain.favorite.service;

import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteFacade {
    private final StoreService storeService;
    private final FavoriteService favoriteService;

    @Transactional
    public void add(User user, Long storeId){
        Store store = storeService.findById(storeId);

        favoriteService.add(user, store);

        storeService.updateFavorites(storeId, 1);
    }

    @Transactional
    public void delete(User user, Long storeId){
        Store store = storeService.findById(storeId);

        favoriteService.delete(user, store);

        storeService.updateFavorites(storeId, -1);
    }
}
