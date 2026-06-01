package com.example.demo.domain.owner.service;

import com.example.demo.domain.favorite.service.FavoriteService;
import com.example.demo.domain.owner.dto.*;
import com.example.demo.domain.owner.dto.ReservationSearchOwnerResponse;
import com.example.demo.domain.reservation.service.ReservationService;
import com.example.demo.domain.schedule.service.ScheduleService;
import com.example.demo.domain.store.domain.Store;
import com.example.demo.domain.store.domain.StoreStatus;
import com.example.demo.domain.store.service.StoreService;
import com.example.demo.domain.storeTable.service.StoreTableService;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.infra.kakao.KakaoLocalClient;
import com.example.demo.global.infra.kakao.PointDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerFacade {
    private final StoreService storeService;
    private final FavoriteService favoriteService;
    private final ReservationService reservationService;
    private final ScheduleService scheduleService;
    private final StoreTableService storeTableService;
    private final KakaoLocalClient kakaoLocalClient;

    @Transactional
    public Page<StoreOwnerResponse> getStores(Long userId, StoreOwnerRequest dto){
        PageRequest pageable = PageRequest.of(dto.page(), dto.size(), Sort.by("id").descending());
        return storeService.getMyStores(userId, pageable)
                .map(store -> {
                    boolean hasSchedule = scheduleService.existsByStoreIdAndStatus(store.getId());
                    boolean hasTable = storeTableService.existsByStoreIdAndStatus(store.getId());
                    return StoreOwnerResponse.from(store, hasSchedule, hasTable);
                });
    }

    @Transactional
    public StoreDetailOwnerResponse getStoreDetail(Long userId, Long storeId){
        Store store = storeService.getMyStore(userId, storeId);
        boolean hasSchedule = scheduleService.existsByStoreIdAndStatus(store.getId());
        boolean hasTable = storeTableService.existsByStoreIdAndStatus(store.getId());
        return StoreDetailOwnerResponse.from(store, hasSchedule, hasTable);
    }

    @Transactional
    public void delete(Long userId, StoreDeleteRequest dto) {
        List<Store> stores = storeService.findByAllId(dto.ids());

        stores.forEach(store -> {
                    storeService.validateOwner(store, userId);
                    storeService.validateStatus(store, StoreStatus.READY, StoreStatus.OPEN, StoreStatus.HIDDEN);
                    reservationService.validateDeletable(store.getId());
        });

        favoriteService.bulkUpdateStatusByStore(dto.ids());
        scheduleService.bulkUpdateStatus(dto.ids());
        storeTableService.bulkUpdateStatus(dto.ids());
        storeService.bulkUpdateStatus(dto.ids());
    }

    @Transactional
    public void updateStoreInfo(Long userId, Long storeId, StoreInfoUpdateRequest dto){
        Store store = storeService.findById(storeId);

        storeService.validateOwner(store, userId);
        storeService.validateStatus(store, StoreStatus.READY, StoreStatus.OPEN, StoreStatus.HIDDEN);

        PointDto coordinates = kakaoLocalClient.getCoordinates(dto.address());

        store.updateBasicInfo(dto.name(), dto.category(), dto.address(), dto.detailAddress(), dto.zipCode(), dto.sigunguCode(), coordinates.latitude(), coordinates.longitude(), dto.phone());
    }

    @Transactional
    public void updateStoreStatus(Long userId, Long storeId, StoreStatusUpdateRequest dto){
        Store store = storeService.findByIdWithLock(storeId);

        storeService.validateOwner(store, userId);
        storeService.validateStatus(store, StoreStatus.READY, StoreStatus.OPEN, StoreStatus.HIDDEN);

        if(dto.status() == StoreStatus.SHUTDOWN)
            throw new BusinessException(ErrorCode.POLICY_VIOLATION);

        if(dto.status() == StoreStatus.OPEN){
            boolean hasSchedule = scheduleService.existsByStoreIdAndStatus(store.getId());
            boolean hasTable = storeTableService.existsByStoreIdAndStatus(store.getId());
            if(!hasSchedule || !hasTable)
                throw new BusinessException(ErrorCode.INVALID_STORE_STATUS, store.getStatus().getDesc());
        }

        store.updateStatus(dto.status());
    }

    public Page<ReservationSearchOwnerResponse> getStoreReservation(Long userId, Long storeId, ReservationSearchOwnerRequest dto){
        Store store = storeService.findById(storeId);

        storeService.validateOwner(store, userId);
        storeService.validateStatus(store, StoreStatus.OPEN);

        LocalDate startDate = dto.startDate()==null ? LocalDate.now().minusMonths(1) : dto.startDate();
        LocalDate endDate = dto.endDate()==null ? LocalDate.now().plusMonths(1) : dto.endDate();

        reservationService.validateDate(startDate, endDate);

        return reservationService.getStoreReservation(store.getId(), dto, startDate, endDate)
                .map(ReservationSearchOwnerResponse::from);
    }

    @Transactional
    public void updateReservationStatus(Long userId, Long storeId, ReservationUpdateOwnerRequest dto){
        Store store = storeService.findByIdWithLock(storeId);

        storeService.validateOwner(store, userId);
        storeService.validateStatus(store, StoreStatus.READY, StoreStatus.OPEN, StoreStatus.HIDDEN);

        reservationService.updateStatus(userId, dto);
    }
}
