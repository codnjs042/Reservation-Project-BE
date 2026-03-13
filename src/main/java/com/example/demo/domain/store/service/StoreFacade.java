package com.example.demo.domain.store.service;

import com.example.demo.domain.favorite.serivce.FavoriteService;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreFacade {
    private final FavoriteService favoriteService;
    private final StoreService storeService;

    public long countFans(Long storeId){
        Store store = storeService.findById(storeId);

        return favoriteService.countFans(store.getId());
    }

    @Transactional
    public void delete(Long userId, Long storeId) {
        Store store = storeService.findById(storeId);

        if (!store.getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        favoriteService.updateStatusByStore(store.getId());

        store.updateStatus(StoreStatus.DELETED);
    }
}
