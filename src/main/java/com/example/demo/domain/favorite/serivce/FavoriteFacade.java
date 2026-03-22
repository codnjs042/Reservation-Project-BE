package com.example.demo.domain.favorite.serivce;

import com.example.demo.domain.favorite.domain.FavoriteStatus;
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
    public void toggle(User user, Long storeId){
        Store store = storeService.findById(storeId);

        FavoriteStatus status = favoriteService.toggle(user, store);

        int delta = (status == FavoriteStatus.ACTIVE) ? 1 : -1;

        storeService.updateFavorites(storeId, delta);
    }
}
