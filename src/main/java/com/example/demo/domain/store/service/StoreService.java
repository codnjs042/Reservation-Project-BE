package com.example.demo.domain.store.service;

import com.example.demo.domain.favorite.domain.FavoriteStatus;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.domain.store.dto.*;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;

    public Store findById(Long storeId){
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }

    @Transactional
    public StoreRegisterResponse register(User user, StoreRegisterRequest dto){
        Optional<Store> existing= storeRepository.findByBusinessNumber(dto.businessNumber());

        if(existing.isPresent())
            throw new BusinessException(ErrorCode.STORE_ALREADY_EXIST);

        Store store = Store.builder()
                .name(dto.name())
                .category(dto.category())
                .address(dto.address())
                .phone(dto.phone())
                .owner(user)
                .businessNumber(dto.businessNumber())
                .slotInterval(dto.slotInterval())
                .usageTime(dto.usageTime())
                .favorites(0)
                .status(StoreStatus.PENDING)
                .build();

        storeRepository.save(store);

        return StoreRegisterResponse.from(store);
    }

    public List<StoreSearchResponse> getList(StoreSearchRequest dto){
        List<Store> store = storeRepository.getList(dto.keyword(), StoreStatus.CONFIRMED);
        return store.stream().map(StoreSearchResponse::from).toList();
    }

    @Transactional
    public void modify(Long userId, Long storeId, StoreUpdateRequest dto){
        Store store = findById(storeId);

        if(!store.getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        store.modify(dto.name(), dto.category(), dto.address(), dto.phone(), dto.slotInterval(), dto.usageTime(), dto.status());
    }

    @Transactional
    public void updateFavorites(Long storeId, int delta){
        storeRepository.updateFavorites(storeId, delta);
    }

    @Transactional
    public void updateAllFavorites(Long userId, int delta) {
        storeRepository.updateAllFavorites(delta, userId, FavoriteStatus.ACTIVE);
    }
}
