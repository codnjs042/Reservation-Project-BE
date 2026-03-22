package com.example.demo.domain.store.service;

import com.example.demo.domain.favorite.serivce.FavoriteService;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.domain.store.dto.StoreRegisterRequest;
import com.example.demo.domain.store.dto.StoreResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreFacade {
    private final FavoriteService favoriteService;
    private final StoreService storeService;
    private final UserService userService;
    private final SecurityUtil securityUtil;

    @Transactional
    public StoreResponse register(User user, StoreRegisterRequest dto){
        Store store = storeService.create(user, dto);

        userService.updateRole(user.getId(), UserRole.OWNER);

        User currentUser = userService.findById(user.getId());
        securityUtil.updateSecurityContext(currentUser);

        return StoreResponse.from(store);
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
