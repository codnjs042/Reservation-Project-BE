package com.example.demo.domain.store.service;

import com.example.demo.domain.admin.dto.StoreAdminResponse;
import com.example.demo.domain.favorite.domain.FavoriteStatus;
import com.example.demo.domain.owner.dto.StoreInfoUpdateRequest;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreCategory;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.domain.store.dto.*;
import com.example.demo.domain.store.repository.StoreRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;

    public Store findById(Long storeId){
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }

    public Store findByIdWithLock(Long storeId){
        return storeRepository.findByIdWithLock(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }

    @Transactional
    public Store create(User user, StoreRegisterRequest dto){
        boolean isExists = storeRepository.existsByBusinessNumberAndStatusNot(dto.businessNumber(), StoreStatus.SHUTDOWN);

        if(isExists)
            throw new BusinessException(ErrorCode.STORE_ALREADY_EXIST);

        Store store = Store.builder()
                .name(dto.name())
                .category(dto.category())
                .address(dto.address())
                .phone(dto.phone())
                .owner(user)
                .ownerName(dto.ownerName())
                .businessNumber(dto.businessNumber())
                .status(StoreStatus.READY)
                .build();

        storeRepository.save(store);

        return store;
    }

    public void validateOwner(Store store, Long userId){
        if(!store.getOwner().getId().equals(userId))
            throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    public void validateStatus(Store store, StoreStatus... allowedStatuses){
        if(!List.of(allowedStatuses).contains(store.getStatus()))
            throw new BusinessException(ErrorCode.INVALID_STORE_STATUS, store.getStatus().getDesc());
    }

    public List<StoreResponse> getList(StoreSearchRequest dto){
        List<StoreCategory> keywordCategories = null;

        if(dto.keyword() != null && !dto.keyword().isEmpty()) {
            keywordCategories = Arrays.stream(StoreCategory.values())
                    .filter(x -> x.getDesc().contains(dto.keyword()))
                    .toList();

            if(keywordCategories.isEmpty())
                keywordCategories = null;
        }

        List<Store> store = storeRepository.getList(dto.keyword(), keywordCategories, dto.category(), List.of(StoreStatus.READY, StoreStatus.OPEN));
        return store.stream().map(StoreResponse::from).toList();
    }

    @Transactional
    public void updateStoreInfo(Long userId, Long storeId, StoreInfoUpdateRequest dto){
        Store store = findById(storeId);

        validateOwner(store, userId);
        validateStatus(store, StoreStatus.READY, StoreStatus.OPEN, StoreStatus.HIDDEN);

        store.updateBasicInfo(dto.name(), dto.category(), dto.phone());
    }

    @Transactional
    public void updateFavorites(Long storeId, int delta){
        storeRepository.updateFavorites(storeId, delta);
    }

    @Transactional
    public void updateAllFavorites(Long userId, int delta) {
        storeRepository.updateAllFavorites(delta, userId, FavoriteStatus.ACTIVE);
    }

    @Transactional
    public void updateAllStores(Long userId){
        storeRepository.updateAllStores(userId, StoreStatus.SHUTDOWN);
    }

    public List<Store> getMyStores(Long userId){
        return storeRepository.getMyStores(userId, StoreStatus.SHUTDOWN);
    }

    public Store getMyStore(Long userId, Long storeId){
        Store store = findById(storeId);

        validateOwner(store, userId);
        validateStatus(store, StoreStatus.READY, StoreStatus.OPEN, StoreStatus.HIDDEN);

        return store;
    }

    public List<Store> findByAllId(List<Long> storeIds){
        return storeRepository.findAllByIdWithLock(storeIds);
    }

    public void bulkUpdateStatus(List<Long> storeIds){
        storeRepository.bulkUpdateStatus(storeIds, StoreStatus.SHUTDOWN);
    }

    public List<StoreResponse> getFamous(){
        Pageable TopSix = PageRequest.of(0, 6);
        List<Store> famousStore = storeRepository.getFamous(List.of(StoreStatus.READY, StoreStatus.OPEN), FavoriteStatus.ACTIVE, TopSix);
        List<Store> latestStore = storeRepository.findTop12ByStatusOrderByCreatedAtDesc(StoreStatus.OPEN);
        Set<Store> combined = new LinkedHashSet<>(famousStore);
        combined.addAll(latestStore);
        return combined.stream()
                .limit(6)
                .map(StoreResponse::from)
                .toList();
    }

    public List<StoreAdminResponse> getStoresForAdmin(String keyword, StoreCategory category, StoreStatus status){
        return storeRepository.getStoresForAdmin(keyword, category, status).stream()
                .map(StoreAdminResponse::from)
                .toList();
    }
}
