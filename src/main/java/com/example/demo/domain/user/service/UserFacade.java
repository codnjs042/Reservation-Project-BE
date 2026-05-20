package com.example.demo.domain.user.service;

import com.example.demo.domain.favorite.serivce.FavoriteService;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFacade {
    private final UserService userService;
    private final StoreService storeService;
    private final FavoriteService favoriteService;

    @Transactional
    public void delete(Long userId){
        User user = userService.findByIdWithLock(userId);

        storeService.updateAllStores(user.getId());

        storeService.updateAllFavorites(user.getId(), -1);

        favoriteService.updateStatusByUser(user.getId());

        user.updateStatus(UserStatus.DELETED);
        user.updateVersion();
    }
}
